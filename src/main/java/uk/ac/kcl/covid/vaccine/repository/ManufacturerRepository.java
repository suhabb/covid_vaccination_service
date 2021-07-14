package uk.ac.kcl.covid.vaccine.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import uk.ac.kcl.covid.vaccine.domain.Manufacturer;



public interface ManufacturerRepository extends ReactiveMongoRepository<Manufacturer, String> {

    Mono<Manufacturer> findByLocation(String location);

    @Query("{ 'iso_code' : {$regex : ?0, $options: 'i'}}")
    Mono<Manufacturer> findByIsoCode(String isoCode);
}
