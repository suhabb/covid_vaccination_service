package uk.ac.kcl.covid.vaccine.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.kcl.covid.vaccine.domain.Country;


public interface CountryRepository extends ReactiveMongoRepository<Country, String> {

    Mono<Country> findByCountry(String country);

    Flux<Country> findAll();
}