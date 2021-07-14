package uk.ac.kcl.covid.vaccine.application_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uk.ac.kcl.covid.vaccine.data_service.ManufacturerDataService;
import uk.ac.kcl.covid.vaccine.data_transfer.ManufacturerDTO;
import uk.ac.kcl.covid.vaccine.domain.Manufacturer;
import uk.ac.kcl.covid.vaccine.mapper.Mapper;

@Service
@Slf4j
public class ManufacturerApplicationService {

    private ManufacturerDataService manufacturerDataService;
    private Mapper mapper;

    public ManufacturerApplicationService(Mapper mapper, ManufacturerDataService manufacturerDataService) {
        this.manufacturerDataService = manufacturerDataService;
        this.mapper = mapper;

    }

    public Mono<ManufacturerDTO> findByIsoCode(String isoCode) {
        Mono<Manufacturer> countryMono = this.manufacturerDataService.findByIsoCode(isoCode);
        return countryMono.map(c -> mapper.readValue(c, ManufacturerDTO.class));
    }

    public Mono<ManufacturerDTO> findByLocation(String location) {
        Mono<Manufacturer> countryMono = this.manufacturerDataService.findByIsoCode(location);
        return countryMono.map(c -> mapper.readValue(c, ManufacturerDTO.class));
    }
}
