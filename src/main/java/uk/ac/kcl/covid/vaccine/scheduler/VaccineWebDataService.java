package uk.ac.kcl.covid.vaccine.scheduler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uk.ac.kcl.covid.vaccine.data_service.CountryDataService;
import uk.ac.kcl.covid.vaccine.data_transfer.CountryDTO;
import uk.ac.kcl.covid.vaccine.domain.Country;
import uk.ac.kcl.covid.vaccine.mapper.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class VaccineWebDataService {

    private CountryDataService countryDataService;

    private Mapper mapper;

    private final WebClient webClient;



    public VaccineWebDataService(CountryDataService countryDataService, Mapper mapper, WebClient.Builder webClientBuilder){
        this.countryDataService=countryDataService;
        this.mapper = mapper;
        this.webClient = webClientBuilder.baseUrl("https://raw.githubusercontent.com").build();
    }

    @Scheduled(fixedRate=60*60*1000)
    public void getUpdatedVaccineDataFromApi(){
            log.info("Vaccine API update started {}:",LocalDate.now());

            Mono<String> countryDTOFlux = this.webClient.get()
                    .uri("/owid/covid-19-data/master/public/data/vaccinations/vaccinations.json")
                    .retrieve()
                    .bodyToMono(String.class);
            log.info("Deleting All Data {}:", LocalDateTime.now());
            countryDataService.deleteAll().subscribe();
            log.info("Starting to add data from API.............  ,Time:{}",LocalDateTime.now());
            countryDTOFlux.map(countryDTO -> {
                List<CountryDTO> countryList = mapper.mapStringToCountryList(countryDTO);
                countryList.stream().forEach(item -> {
                    Country countryEntity = mapper.mapToCountry(item);
                    countryDataService.save(countryEntity).subscribe();
                });
                return countryList;
            }).log().subscribe();

    }

}
