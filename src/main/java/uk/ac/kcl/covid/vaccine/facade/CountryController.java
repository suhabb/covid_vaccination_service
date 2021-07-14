package uk.ac.kcl.covid.vaccine.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.ac.kcl.covid.vaccine.application_service.CountryApplicationService;
import uk.ac.kcl.covid.vaccine.data_transfer.CountryDTO;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class CountryController {

    private CountryApplicationService countryApplicationService;

    public CountryController(CountryApplicationService countryApplicationService){
        this.countryApplicationService = countryApplicationService;
    }

    @GetMapping("/country/search")
    public Mono<ResponseEntity<List<CountryDTO>>> search(@RequestParam("iso-code") Optional<String> isoCode) {

        Flux<CountryDTO> fluxCountryDto = countryApplicationService.search(isoCode);
        return fluxCountryDto.collectList().map(ResponseEntity::ok);

    }

    @GetMapping("/country/iso-code/{isoCode}")
    public Mono<ResponseEntity<CountryDTO>> findByIsoCode(@PathVariable("isoCode") String isoCode) {
        Mono<CountryDTO> monoCountryDto = countryApplicationService.findByIsoCode(isoCode);
        return monoCountryDto.map(ResponseEntity::ok );
    }

    @GetMapping("/country/{country}")
    public Mono<ResponseEntity<CountryDTO>> findByBlockId(@PathVariable("country") String country) {

        Mono<CountryDTO> monoCountryDto = countryApplicationService.findByCountry(country);
        return monoCountryDto.map( ResponseEntity::ok );
    }

    @GetMapping("/health")
    public String health() {
        return "Ok";
    }

}
