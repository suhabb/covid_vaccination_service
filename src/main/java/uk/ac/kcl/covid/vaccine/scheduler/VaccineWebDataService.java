package uk.ac.kcl.covid.vaccine.scheduler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uk.ac.kcl.covid.vaccine.data_service.CountryDataService;
import uk.ac.kcl.covid.vaccine.data_service.ManufacturerDataService;
import uk.ac.kcl.covid.vaccine.data_transfer.CountryDTO;
import uk.ac.kcl.covid.vaccine.data_transfer.ManufacturerDTO;
import uk.ac.kcl.covid.vaccine.data_transfer.VaccineCompanyDTO;
import uk.ac.kcl.covid.vaccine.domain.Country;
import uk.ac.kcl.covid.vaccine.domain.Manufacturer;
import uk.ac.kcl.covid.vaccine.mapper.Mapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    // @Scheduled(fixedRate=60*60*1000)
    public void getUpdatedVaccineDataFromApi() {
        log.info("Vaccine API update started {}:", LocalDate.now());

        Mono<String> countryDTOFlux = this.webClient.get()
                .uri("/owid/covid-19-data/master/public/data/vaccinations/vaccinations.json")
                .retrieve()
                .bodyToMono(String.class);
        log.info("Deleting All Data {}:", LocalDateTime.now());
        countryDataService.deleteAll().subscribe();
        log.info("Starting to add data from API.............  ,Time:{}", LocalDateTime.now());
        countryDTOFlux.map(countryDTO -> {
            List<CountryDTO> countryList = mapper.mapStringToCountryList(countryDTO);
            countryList.stream().forEach(item -> {
                Country countryEntity = mapper.mapToCountry(item);
                countryDataService.save(countryEntity).subscribe();
            });
            return countryList;
        }).log().subscribe();

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
                    log.info("Saved to database:{}",manufacturer.toString());
                });
            }
        }
    }

    @Scheduled(fixedRate=60*60*1000)
    public void generateMockVaccineManufacturerData() throws IOException {

        File resource = new ClassPathResource(
                "json/mock_vaccine_manufacturer_country_data.json").getFile();
        String manufacturerData = new String(
                Files.readAllBytes(resource.toPath()));
        List<Manufacturer> manufacturerList = this.mapper.mapToManufacturerDTO(manufacturerData);
        manufacturerDataService.deleteAll().subscribe();
        manufacturerList.forEach(manufacturer -> {
            manufacturerDataService.save(manufacturer).subscribe();
            log.info("Saved to database:{}",manufacturer.toString());
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
