package uk.ac.kcl.covid.vaccine.data_service;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.kcl.covid.vaccine.domain.Manufacturer;
import uk.ac.kcl.covid.vaccine.repository.ManufacturerRepository;

import java.util.Optional;

@Service
public class ManufacturerDataService {

    public static final String ISO_CODE = "iso_code";
    private ManufacturerRepository manufacturerRepository;

    private ReactiveMongoTemplate reactiveMongoTemplate;

    public ManufacturerDataService(ReactiveMongoTemplate reactiveMongoTemplate,ManufacturerRepository manufacturerRepository){
        this.manufacturerRepository = manufacturerRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    public Flux<Manufacturer> search(Optional<String> isoCode){

        Criteria criteria = new Criteria();
        if(isoCode.isPresent()){
            String value = isoCode.get();
            criteria.and(ISO_CODE).regex(value,"i");
        }
        Flux<Manufacturer> manufacturerFlux = reactiveMongoTemplate.find(new Query(criteria), Manufacturer.class);
        return manufacturerFlux;
    }

    public Mono<Manufacturer> findByIsoCode(String isoCode){
        return this.manufacturerRepository.findByIsoCode(isoCode);
    }

    public Mono<Manufacturer> save(Manufacturer manufacturer){
        return this.manufacturerRepository.save(manufacturer);
    }

    public Mono<Void> deleteAll(){
        return this.manufacturerRepository.deleteAll();
    }

    public Mono<Manufacturer> findByLocation(String location){
        return this.manufacturerRepository.findByLocation(location);
    }
}
