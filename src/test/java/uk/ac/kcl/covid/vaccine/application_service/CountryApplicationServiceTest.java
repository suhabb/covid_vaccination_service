package uk.ac.kcl.covid.vaccine.application_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import uk.ac.kcl.covid.vaccine.data_service.CountryDataService;
import uk.ac.kcl.covid.vaccine.data_transfer.CountryDTO;
import uk.ac.kcl.covid.vaccine.domain.Country;
import uk.ac.kcl.covid.vaccine.mapper.Mapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
public class CountryApplicationServiceTest {

    private CountryApplicationService countryApplicationService;

    private CountryDataService countryDataService=mock(CountryDataService.class);
    private Mapper mapper = new Mapper(new ObjectMapper());


    @BeforeEach()
    public void setUp(){
        countryApplicationService = new CountryApplicationService(mapper,countryDataService);
    }


    @Test
    public void given_iso_code_return_country_mono() throws IOException {
        File resource = new ClassPathResource(
                "json/country_vaccination.json").getFile();
        String mockTimelineJson = new String(Files.readAllBytes(resource.toPath()));

        Country country = mapToCountry(mockTimelineJson);
        when(countryDataService.findByIsoCode(anyString())).thenReturn(Mono.just(country));
        Mono<CountryDTO> countryDTOMono = countryApplicationService.findByIsoCode("GBR");
        CountryDTO countryDTO = mapper.mapStringToCountry(mockTimelineJson);
        StepVerifier
                .create( countryDTOMono )
                .expectNext(countryDTO)
                .expectComplete()
                .verify();
    }

    public Country mapToCountry(String countryDTO) {

        try {
            return new ObjectMapper().readValue(countryDTO, Country.class);
        } catch (IOException exception) {
            log.debug("Json exception read value method", exception);
            throw new RuntimeException(exception);
        }
    }
}
