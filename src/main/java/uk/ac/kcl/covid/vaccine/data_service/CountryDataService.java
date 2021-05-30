package uk.ac.kcl.covid.vaccine.data_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.kcl.covid.vaccine.domain.Country;
import uk.ac.kcl.covid.vaccine.repository.CountryRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CountryDataService {

    public static final String ISO_CODE = "iso_code";
    private CountryRepository countryRepository;

    private ReactiveMongoTemplate reactiveMongoTemplate;

    public CountryDataService(ReactiveMongoTemplate reactiveMongoTemplate,CountryRepository countryRepository){
        this.countryRepository = countryRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    public Flux<Country> search(Optional<String> isoCode){

        Criteria criteria = new Criteria();
        if(isoCode.isPresent()){
            String value = isoCode.get();
            criteria.and(ISO_CODE).regex(value,"i");
        }
        Flux<Country> countryList = reactiveMongoTemplate.find(new Query(criteria), Country.class);
        return countryList;
    }

    public Mono<Country> findByCountry(String country){
        return this.countryRepository.findByCountry(country);
    }
}
