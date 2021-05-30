package uk.ac.kcl.covid.vaccine.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.kcl.covid.vaccine.data_transfer.CountryDTO;
import uk.ac.kcl.covid.vaccine.domain.Country;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class Mapper {

    @Autowired
    public ObjectMapper objectMapper;

    public <T> T readValue(Object object, Class<T> clazz) {
        try {
            return objectMapper.readValue(writeValueAsString(object), clazz);
        } catch (IOException exception) {
            log.debug("Json exception read value method", exception);
            throw new RuntimeException(exception);
        }
    }

    public String writeValueAsString(Object clazz) {
        try {
            return objectMapper.writeValueAsString(clazz);
        } catch (JsonProcessingException exception) {
            log.debug("Json exception write value as string method", exception);
            throw new RuntimeException(exception);
        }
    }

    public String writeValueAsString(List<Country> countryList) {
        try {
            return objectMapper.writeValueAsString(countryList);
        } catch (JsonProcessingException exception) {
            log.debug("Json exception write value as string method", exception);
            throw new RuntimeException(exception);
        }
    }


    public List<CountryDTO> mapToCountryDtoList(List<Country> countryList) {
        try {
            return objectMapper.readValue(writeValueAsString(countryList), new TypeReference<List<CountryDTO>>(){});
        } catch (IOException exception) {
            log.debug("Json exception read value method", exception);
            throw new RuntimeException(exception);
        }
    }
}
