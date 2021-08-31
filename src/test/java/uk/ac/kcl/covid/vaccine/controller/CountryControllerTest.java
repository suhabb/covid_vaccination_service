package uk.ac.kcl.covid.vaccine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import uk.ac.kcl.covid.vaccine.application_service.CountryApplicationService;
import uk.ac.kcl.covid.vaccine.data_transfer.CountryDTO;
import uk.ac.kcl.covid.vaccine.facade.CountryController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class CountryControllerTest {

    private CountryApplicationService countryApplicationService = mock(CountryApplicationService.class);

    private CountryController countryController;

    @BeforeEach
    public void setUp() {
        this.countryController = new CountryController(countryApplicationService);
    }


    @Test
    public void given_uri_return_ok_response() throws IOException {
        File resource = new ClassPathResource(
                "json/country_vaccination.json").getFile();
        String mockTimelineJson = new String(Files.readAllBytes(resource.toPath()));

        CountryDTO countryDTO = mapToCountryDTO(mockTimelineJson);
        when(countryApplicationService.findByIsoCode(anyString())).thenReturn(Mono.just(countryDTO));
        Mono<ResponseEntity<CountryDTO>> entityMono = countryController.findByIsoCode("GBR");
        StepVerifier
                .create(entityMono)
                .expectNext(ResponseEntity.ok(countryDTO))
                .expectComplete()
                .verify();


    }

    public CountryDTO mapToCountryDTO(String country) {
        try {
            return new ObjectMapper().readValue(country, CountryDTO.class);
        } catch (IOException exception) {
            log.debug("Json exception read value method", exception);
            throw new RuntimeException(exception);
        }
    }
}
