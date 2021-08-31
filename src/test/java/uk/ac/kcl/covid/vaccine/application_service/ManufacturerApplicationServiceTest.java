package uk.ac.kcl.covid.vaccine.application_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import uk.ac.kcl.covid.vaccine.data_service.ManufacturerDataService;
import uk.ac.kcl.covid.vaccine.data_transfer.ManufacturerDTO;
import uk.ac.kcl.covid.vaccine.domain.Manufacturer;
import uk.ac.kcl.covid.vaccine.mapper.Mapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class ManufacturerApplicationServiceTest {

    private ManufacturerApplicationService manufacturerApplicationService;

    private ManufacturerDataService manufacturerDataService=mock(ManufacturerDataService.class);
    private Mapper mapper = new Mapper(new ObjectMapper());


    @BeforeEach()
    public void setUp(){
        manufacturerApplicationService = new ManufacturerApplicationService(mapper,manufacturerDataService);
    }


    @Test
    public void given_iso_code_return_manufacturer_mono() throws IOException {
        File resource = new ClassPathResource(
                "json/manufacturer_vaccine.json").getFile();
        String mockManufacturerJson = new String(Files.readAllBytes(resource.toPath()));

        Manufacturer manufacturer = mapToManufacturer(mockManufacturerJson);
        when(manufacturerDataService.findByIsoCode(anyString())).thenReturn(Mono.just(manufacturer));
        Mono<ManufacturerDTO> manufacturerDTOMono = manufacturerApplicationService.findByIsoCode("GBR");
        ManufacturerDTO manufacturerDTO = mapper.mapToStringManufacturerDTO(mockManufacturerJson);
        StepVerifier
                .create( manufacturerDTOMono )
                .expectNext(manufacturerDTO)
                .expectComplete()
                .verify();
    }

    public Manufacturer mapToManufacturer(String manufacturerDTO) {

        try {
            return new ObjectMapper().readValue(manufacturerDTO, Manufacturer.class);
        } catch (IOException exception) {
            log.debug("Json exception read value method", exception);
            throw new RuntimeException(exception);
        }
    }
}
