package uk.ac.kcl.covid.vaccine.scheduler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import uk.ac.kcl.covid.vaccine.data_service.CountryDataService;
import uk.ac.kcl.covid.vaccine.data_service.ManufacturerDataService;
import uk.ac.kcl.covid.vaccine.data_transfer.CountryDTO;
import uk.ac.kcl.covid.vaccine.data_transfer.ManufacturerDTO;
import uk.ac.kcl.covid.vaccine.data_transfer.VaccineCompanyDTO;
import uk.ac.kcl.covid.vaccine.domain.Country;
import uk.ac.kcl.covid.vaccine.domain.Manufacturer;
import uk.ac.kcl.covid.vaccine.domain.Vaccine;
import uk.ac.kcl.covid.vaccine.domain.VaccineCompany;
import uk.ac.kcl.covid.vaccine.mapper.Mapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class VaccineWebDataService {

    private CountryDataService countryDataService;

    private ManufacturerDataService manufacturerDataService;

    private Mapper mapper;

    private final WebClient webClient;

    private HashMap<String, String> mapOfIso3Countries = new HashMap<>();

    private Environment environment;


    public VaccineWebDataService(CountryDataService countryDataService, Mapper mapper, WebClient.Builder webClientBuilder,
                                 ManufacturerDataService manufacturerDataService, Environment environment) {
        this.countryDataService = countryDataService;
        this.manufacturerDataService = manufacturerDataService;
        this.mapper = mapper;
        this.webClient = webClientBuilder.baseUrl("https://raw.githubusercontent.com").build();
        this.environment = environment;
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void getUpdatedVaccineDataFromApi() throws InterruptedException {
        log.info("Vaccine API update started {}:", LocalDate.now());

        Mono<String> countryDTOMono = this.webClient.get()
                .uri("/owid/covid-19-data/master/public/data/vaccinations/vaccinations.json")
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)));
        log.info("Deleting All Data {}:", LocalDateTime.now());
        countryDataService.deleteAll().subscribe();
        log.info("Starting to add data from API.............  ,Time:{}", LocalDateTime.now());
        countryDTOMono.map(countryDTO -> {
            List<CountryDTO> countryList = mapper.mapStringToCountryList(countryDTO);
            countryList.stream().forEach(item -> {
                Country countryEntity = mapper.mapToCountry(item);
                countryDataService.save(countryEntity).subscribe();
            });
            return countryList;
        }).subscribe();
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void generateMockVaccineManufacturerDataFromVaccine() throws InterruptedException {
        Thread.sleep(20000);
        Flux<Country> countryFlux = countryDataService.findAll();
        manufacturerDataService.deleteAll().subscribe();
        log.info("Start to Generate Mock data for manufacturer ");
        List<String> isoCodeList = Arrays.asList("RUS", "CHN", "ARE", "IND", "PAK","GBR");
        countryFlux.mapNotNull(country -> {
            Manufacturer manufacturer = new Manufacturer();
            manufacturer.setIsoCode(country.getIsoCode());
            manufacturer.setLocation(country.getCountry());
            Vaccine vaccine = country.getData().get(country.getData().size() - 1);
            if (Objects.nonNull(vaccine)) {
                boolean isAvailable = isoCodeList.stream()
                        .anyMatch(manufacturer.getIsoCode()::equalsIgnoreCase);
                if (isAvailable) {
                    createRandomDataForSomeCountries(manufacturer, vaccine);
                } else {
                    createRandomData(manufacturer, vaccine);
                }
            }
            log.info("Save data for manufacturer");
            manufacturerDataService.save(manufacturer).subscribe();
            return country;
        }).subscribe();
    }

    private void createRandomDataForSomeCountries(Manufacturer manufacturer, Vaccine vaccine) {

        if (manufacturer.getIsoCode().equalsIgnoreCase("IND")) {
            int[] selectedArray = new int[]{25, 60, 15};
            List<String> vaccineCompanyNameList = Arrays.asList("Covaxin",
                    "Covishield/AstraZeneca", "Sputnik");
            List<VaccineCompany> vaccineCompanies = new ArrayList<>();
            IntStream.range(0, vaccineCompanyNameList.size()).forEach(index -> {
                VaccineCompany vaccineCompany = new VaccineCompany();
                int percentage = selectedArray[index];
                Long vaccineOfEachCompany = getVaccinationOfEachCompany(percentage, vaccine.getTotalVaccinations());
                String vaccineCompanyString = vaccineCompanyNameList.get(index);
                vaccineCompany.setTotalVaccinations(vaccineOfEachCompany);
                vaccineCompany.setVaccine(vaccineCompanyString);
                vaccineCompany.setDate(vaccine.getDate());
                vaccineCompanies.add(vaccineCompany);
            });
            manufacturer.setVaccineCompanies(vaccineCompanies);
        } else if (manufacturer.getIsoCode().equalsIgnoreCase("RUS")) {
            int[] selectedArray = new int[]{10, 10, 80};
            List<String> vaccineCompanyNameList = Arrays.asList("Pfizer",
                    "Covishield/AstraZeneca", "Sputnik");
            List<VaccineCompany> vaccineCompanies = new ArrayList<>();
            IntStream.range(0, vaccineCompanyNameList.size()).forEach(index -> {
                VaccineCompany vaccineCompany = new VaccineCompany();
                int percentage = selectedArray[index];
                Long vaccineOfEachCompany = getVaccinationOfEachCompany(percentage, vaccine.getTotalVaccinations());
                String vaccineCompanyString = vaccineCompanyNameList.get(index);
                vaccineCompany.setTotalVaccinations(vaccineOfEachCompany);
                vaccineCompany.setVaccine(vaccineCompanyString);
                vaccineCompany.setDate(vaccine.getDate());
                vaccineCompanies.add(vaccineCompany);
            });
            manufacturer.setVaccineCompanies(vaccineCompanies);
        } else if (manufacturer.getIsoCode().equalsIgnoreCase("CHN")) {
            int[] selectedArray = new int[]{80, 10, 10};
            List<String> vaccineCompanyNameList = Arrays.asList("Sinovac",
                    "Oxford/AstraZeneca", "Sputnik");
            List<VaccineCompany> vaccineCompanies = new ArrayList<>();
            IntStream.range(0, vaccineCompanyNameList.size()).forEach(index -> {
                VaccineCompany vaccineCompany = new VaccineCompany();
                int percentage = selectedArray[index];
                Long vaccineOfEachCompany = getVaccinationOfEachCompany(percentage, vaccine.getTotalVaccinations());
                String vaccineCompanyString = vaccineCompanyNameList.get(index);
                vaccineCompany.setTotalVaccinations(vaccineOfEachCompany);
                vaccineCompany.setVaccine(vaccineCompanyString);
                vaccineCompany.setDate(vaccine.getDate());
                vaccineCompanies.add(vaccineCompany);
            });
            manufacturer.setVaccineCompanies(vaccineCompanies);
        } else if (manufacturer.getIsoCode().equalsIgnoreCase("PAK")) {
            int[] selectedArray = new int[]{70, 20, 5, 5};
            List<String> vaccineCompanyNameList = Arrays.asList("Sinovac",
                    "Oxford/AstraZeneca", "Sputnik", "Pfizer");
            List<VaccineCompany> vaccineCompanies = new ArrayList<>();
            IntStream.range(0, vaccineCompanyNameList.size()).forEach(index -> {
                VaccineCompany vaccineCompany = new VaccineCompany();
                int percentage = selectedArray[index];
                Long vaccineOfEachCompany = getVaccinationOfEachCompany(percentage, vaccine.getTotalVaccinations());
                String vaccineCompanyString = vaccineCompanyNameList.get(index);
                vaccineCompany.setTotalVaccinations(vaccineOfEachCompany);
                vaccineCompany.setVaccine(vaccineCompanyString);
                vaccineCompany.setDate(vaccine.getDate());
                vaccineCompanies.add(vaccineCompany);
            });
            manufacturer.setVaccineCompanies(vaccineCompanies);
        } else if (manufacturer.getIsoCode().equalsIgnoreCase("ARE")) {
            int[] selectedArray = new int[]{70, 20, 5, 5};
            List<String> vaccineCompanyNameList = Arrays.asList("Sinovac",
                    "Oxford/AstraZeneca", "Sputnik", "Pfizer");
            List<VaccineCompany> vaccineCompanies = new ArrayList<>();
            IntStream.range(0, vaccineCompanyNameList.size()).forEach(index -> {
                VaccineCompany vaccineCompany = new VaccineCompany();
                int percentage = selectedArray[index];
                Long vaccineOfEachCompany = getVaccinationOfEachCompany(percentage, vaccine.getTotalVaccinations());
                String vaccineCompanyString = vaccineCompanyNameList.get(index);
                vaccineCompany.setTotalVaccinations(vaccineOfEachCompany);
                vaccineCompany.setVaccine(vaccineCompanyString);
                vaccineCompany.setDate(vaccine.getDate());
                vaccineCompanies.add(vaccineCompany);
            });
            manufacturer.setVaccineCompanies(vaccineCompanies);
        } else if (manufacturer.getIsoCode().equalsIgnoreCase("GBR")) {
            int[] selectedArray = new int[]{20, 40, 35, 5};
            List<String> vaccineCompanyNameList = Arrays.asList("Moderna",
                    "Oxford/AstraZeneca", "Pfizer", "Johnson&Johnson");
            List<VaccineCompany> vaccineCompanies = new ArrayList<>();
            IntStream.range(0, vaccineCompanyNameList.size()).forEach(index -> {
                VaccineCompany vaccineCompany = new VaccineCompany();
                int percentage = selectedArray[index];
                Long vaccineOfEachCompany = getVaccinationOfEachCompany(percentage, vaccine.getTotalVaccinations());
                String vaccineCompanyString = vaccineCompanyNameList.get(index);
                vaccineCompany.setTotalVaccinations(vaccineOfEachCompany);
                vaccineCompany.setVaccine(vaccineCompanyString);
                vaccineCompany.setDate(vaccine.getDate());
                vaccineCompanies.add(vaccineCompany);
            });
            manufacturer.setVaccineCompanies(vaccineCompanies);
        }
    }

    private void createRandomData(Manufacturer manufacturer, Vaccine vaccine) {

        int[][] arrays = new int[4][];
        arrays[0] = new int[]{25, 33, 25, 17};
        arrays[1] = new int[]{10, 23, 30, 37};
        arrays[2] = new int[]{19, 21, 40, 20};
        arrays[3] = new int[]{35, 15, 20, 30};
        Random random = new Random();
        int number = random.nextInt(4);
        int[] selectedArray = arrays[number];
        List<String> vaccineCompanyNameList = Arrays.asList("Johnson&Johnson", "Moderna",
                "Oxford/AstraZeneca", "Pfizer");

        List<VaccineCompany> vaccineCompanies = new ArrayList<>();
        IntStream.range(0, vaccineCompanyNameList.size()).forEach(index -> {
            VaccineCompany vaccineCompany = new VaccineCompany();
            int percentage = selectedArray[index];
            Long vaccineOfEachCompany = getVaccinationOfEachCompany(percentage, vaccine.getTotalVaccinations());
            String vaccineCompanyString = vaccineCompanyNameList.get(index);
            vaccineCompany.setTotalVaccinations(vaccineOfEachCompany);
            vaccineCompany.setVaccine(vaccineCompanyString);
            vaccineCompany.setDate(vaccine.getDate());
            vaccineCompanies.add(vaccineCompany);
        });
        manufacturer.setVaccineCompanies(vaccineCompanies);
    }

    public Long getVaccinationOfEachCompany(int percentage, Long total) {
        if (total == null) {
            return null;
        }
        return (percentage * total) / 100;
    }


  /*  @Scheduled(fixedDelay = 60*60*1000)
    public void getManufacturerVaccineDataFromApi() throws Exception {
        log.info("Manufacturer API update started {}:", LocalDate.now());
        Mono<String> vaccineDTOFlux = this.webClient.get()
                .uri("/owid/covid-19-data/master/public/data/vaccinations/vaccinations-by-manufacturer.csv")
                .retrieve()
                .bodyToMono(String.class);
        Pattern pattern = Pattern.compile(",");
        Optional<String> blockOptional = vaccineDTOFlux.blockOptional();
        if (blockOptional.isPresent()) {
            try (BufferedReader in = new BufferedReader(new StringReader(blockOptional.get()))) {
                List<ManufacturerDTO> manufacturersList = in.lines().skip(1).map(line -> {
                    String[] x = pattern.split(line);
                    Date date = null;
                    try {
                        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        simpleDateFormat.applyPattern("yyyy-MM-dd");
                        date = simpleDateFormat.parse(x[1]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return new ManufacturerDTO((x[0]), date, x[2], new Long(x[3]));
                }).collect(Collectors.toList());
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                mapper.writeValue(System.out, vaccineDTOFlux);
                Map<String, List<ManufacturerDTO>> groupByCountry = manufacturersList.stream()
                        .collect(groupingBy(ManufacturerDTO::getLocation, toList()));
                log.info("Country:{}", groupByCountry);
                buildMapOfIso3Countries();
                List<Map<String, List<ManufacturerDTO>>> listOfMap = new ArrayList<>();
                groupByCountry.entrySet().forEach(item -> {
                    List<ManufacturerDTO> manufacturerDTOList = item.getValue();
                    Date maxDate = manufacturerDTOList.stream().map(ManufacturerDTO::getDate)
                            .max(Date::compareTo).get();
                    List<ManufacturerDTO> updatedManufactureDto = manufacturerDTOList.stream().filter(dto -> dto.getDate().equals(maxDate))
                            .collect(Collectors.toList());
                    Map<String, List<ManufacturerDTO>> map = updatedManufactureDto.stream().map(dto -> {
                        String isoCode3 = this.mapOfIso3Countries.get(dto.getLocation());
                        if(isoCode3 == null || isoCode3.isEmpty()){
                            isoCode3 = dto.getLocation();
                        }
                    //    dto.setIsoCode(isoCode3);
                        return dto;
                    }).collect(groupingBy(ManufacturerDTO::getIsoCode, toList()));
                    listOfMap.add(map);
                });
                log.info("listOfMap:{}",listOfMap.toString());
            }
        }
    }*/

    // @Scheduled(fixedDelay = 10000)
    public void getManufacturerVaccineDataFromApi1() throws Exception {
        log.info("Manufacturer API update started {}:", LocalDate.now());
        Mono<String> vaccineDTOFlux = this.webClient.get()
                .uri("/owid/covid-19-data/master/public/data/vaccinations/vaccinations-by-manufacturer.csv")
                .retrieve()
                .bodyToMono(String.class);
        Pattern pattern = Pattern.compile(",");
        Optional<String> blockOptional = vaccineDTOFlux.blockOptional();
        if (blockOptional.isPresent()) {
            try (BufferedReader in = new BufferedReader(new StringReader(blockOptional.get()))) {
                List<VaccineCompanyDTO> manufacturersList = in.lines().skip(1).map(line -> {
                    String[] x = pattern.split(line);
                    Date date = null;
                    try {
                        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        simpleDateFormat.applyPattern("yyyy-MM-dd");
                        date = simpleDateFormat.parse(x[1]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return new VaccineCompanyDTO((x[0]), date, x[2], new Long(x[3]));
                }).collect(Collectors.toList());
                Map<String, List<VaccineCompanyDTO>> groupByCountry = manufacturersList.stream()
                        .collect(groupingBy(VaccineCompanyDTO::getLocation, toList()));
                log.info("Country:{}", groupByCountry);
                buildMapOfIso3Countries();
                List<ManufacturerDTO> manufacturerDtoList = new ArrayList<>();
                groupByCountry.entrySet().forEach(item -> {
                    List<VaccineCompanyDTO> manufacturerDTOList = item.getValue();
                    Date maxDate = manufacturerDTOList.stream().map(VaccineCompanyDTO::getDate)
                            .max(Date::compareTo).get();
                    List<VaccineCompanyDTO> updatedManufactureDto = manufacturerDTOList.stream().filter(dto ->
                            dto.getDate().equals(maxDate))
                            .collect(Collectors.toList());
                    ManufacturerDTO manufacturerDTO = new ManufacturerDTO();
                    List<VaccineCompanyDTO> vaccineCompanyDTOS = updatedManufactureDto.stream().map(dto -> {
                        String isoCode3 = this.mapOfIso3Countries.get(dto.getLocation());
                        if (isoCode3 == null || isoCode3.isEmpty()) {
                            isoCode3 = dto.getLocation();
                        }
                        manufacturerDTO.setLocation(dto.getLocation());
                        manufacturerDTO.setIsoCode(isoCode3);
                        return dto;
                    }).collect(Collectors.toList());
                    manufacturerDTO.setVaccineCompanies(vaccineCompanyDTOS);
                    manufacturerDtoList.add(manufacturerDTO);
                });
                log.debug("Manufacture List:{}", manufacturerDtoList);
                manufacturerDtoList.forEach(manufacturerDTO -> {
                    Manufacturer manufacturer = mapper.mapToManufacturerDTO(manufacturerDTO);
                    manufacturerDataService.save(manufacturer).subscribe();
                    log.info("Saved to database:{}", manufacturer.toString());
                });
            }
        }

    }

    // @Scheduled(fixedRate=60*60*1000)
    public void generateMockVaccineManufacturerData() throws IOException {

        File resource = new ClassPathResource(
                "json/mock_vaccine_manufacturer_country_data.json").getFile();
        String manufacturerData = new String(
                Files.readAllBytes(resource.toPath()));
        List<Manufacturer> manufacturerList = this.mapper.mapToManufacturerDTO(manufacturerData);
        manufacturerDataService.deleteAll().subscribe();
        manufacturerList.forEach(manufacturer -> {
            manufacturerDataService.save(manufacturer).subscribe();
            log.info("Saved to database:{}", manufacturer.toString());
        });
    }

    public HashMap<String, String> buildMapOfIso3Countries() {

        String[] countryCodes = Locale.getISOCountries();
        for (String cc : countryCodes) {
            // country name , country code map
            Locale locale = new Locale("", cc);
            mapOfIso3Countries.put(new Locale("", cc).getDisplayCountry(), locale.getISO3Country()
                    .toUpperCase());
        }
        return mapOfIso3Countries;
    }
}
