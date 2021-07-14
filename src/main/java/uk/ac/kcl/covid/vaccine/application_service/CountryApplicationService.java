package uk.ac.kcl.covid.vaccine.application_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.kcl.covid.vaccine.data_service.CountryDataService;
import uk.ac.kcl.covid.vaccine.data_transfer.CountryDTO;
import uk.ac.kcl.covid.vaccine.domain.Country;
import uk.ac.kcl.covid.vaccine.mapper.Mapper;

import java.util.Optional;

@Service
@Slf4j
public class CountryApplicationService {

    private CountryDataService countryDataService;
    private Mapper mapper;

    public CountryApplicationService(Mapper mapper, CountryDataService countryDataService) {
        this.countryDataService = countryDataService;
        this.mapper = mapper;

    }

    public Flux<CountryDTO> search(Optional<String> isoCode) {
        Flux<Country> countryFlux = this.countryDataService.search(isoCode);
        return countryFlux.collectList().map(mapper::mapToCountryDtoList).flatMapMany(Flux::fromIterable);
    }

    public Mono<CountryDTO> findByIsoCode(String isoCode) {
        Mono<Country> countryMono = this.countryDataService.findByIsoCode(isoCode);
        return countryMono.map(c -> mapper.readValue(c, CountryDTO.class));
    }

    public Mono<CountryDTO> findByCountry(String country) {
        Mono<Country> countryMono = this.countryDataService.findByCountry(country);
        return countryMono.map(c -> mapper.readValue(c, CountryDTO.class));
    }


}
