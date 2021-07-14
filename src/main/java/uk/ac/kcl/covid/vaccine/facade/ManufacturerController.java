package uk.ac.kcl.covid.vaccine.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import uk.ac.kcl.covid.vaccine.application_service.ManufacturerApplicationService;
import uk.ac.kcl.covid.vaccine.data_transfer.ManufacturerDTO;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class ManufacturerController {

    private ManufacturerApplicationService manufacturerApplicationService;

    public ManufacturerController(ManufacturerApplicationService manufacturerApplicationService){
        this.manufacturerApplicationService = manufacturerApplicationService;
    }

    @GetMapping("/manufacturer/iso-code/{isoCode}")
    public Mono<ResponseEntity<ManufacturerDTO>> findByIsoCode(@PathVariable("isoCode") String isoCode) {
        Mono<ManufacturerDTO> manufacturerDTOMono = manufacturerApplicationService.findByIsoCode(isoCode);
        return manufacturerDTOMono.map(ResponseEntity::ok );
    }

    @GetMapping("/manufacturer/{location}")
    public Mono<ResponseEntity<ManufacturerDTO>> findByLocation(@PathVariable("location") String location) {
        Mono<ManufacturerDTO> monoCountryDto = manufacturerApplicationService.findByLocation(location);
        return monoCountryDto.map( ResponseEntity::ok );
    }

    @GetMapping("/manufacturer/health")
    public String health() {
        return "Manufacturer Ok";
    }

}
