package uk.ac.kcl.covid.vaccine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import uk.ac.kcl.covid.vaccine.application_service.ManufacturerApplicationService;
import uk.ac.kcl.covid.vaccine.data_transfer.ManufacturerDTO;
import uk.ac.kcl.covid.vaccine.facade.ManufacturerController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class ManufacturerControllerTest {

    private ManufacturerApplicationService manufacturerApplicationService = mock(ManufacturerApplicationService.class);

    private ManufacturerController manufacturerController;

    @BeforeEach
    public void setUp() {
        this.manufacturerController = new ManufacturerController(manufacturerApplicationService);
    }

    @Test
    public void given_uri_return_ok_response() throws IOException {
        File resource = new ClassPathResource(
                "json/manufacturer_vaccine.json").getFile();
        String mockTimelineJson = new String(Files.readAllBytes(resource.toPath()));

        ManufacturerDTO manufacturerDTO = mapToManufacturerDTO(mockTimelineJson);
        when(manufacturerApplicationService.findByIsoCode(anyString())).thenReturn(Mono.just(manufacturerDTO));
        Mono<ResponseEntity<ManufacturerDTO>> entityMono = manufacturerController.findByIsoCode("GBR");
        StepVerifier
                .create(entityMono)
                .expectNext(ResponseEntity.ok(manufacturerDTO))
                .expectComplete()
                .verify();
    }

    public ManufacturerDTO mapToManufacturerDTO(String manufacturer) {
        try {
            return new ObjectMapper().readValue(manufacturer, ManufacturerDTO.class);
        } catch (IOException exception) {
            log.debug("Json exception read value method", exception);
            throw new RuntimeException(exception);
        }
    }
}
