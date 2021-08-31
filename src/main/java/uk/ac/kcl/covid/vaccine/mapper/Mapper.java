package uk.ac.kcl.covid.vaccine.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.ac.kcl.covid.vaccine.data_transfer.CountryDTO;
import uk.ac.kcl.covid.vaccine.data_transfer.ManufacturerDTO;
import uk.ac.kcl.covid.vaccine.domain.Country;
import uk.ac.kcl.covid.vaccine.domain.Manufacturer;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class Mapper {

    private ObjectMapper objectMapper;

    public Mapper(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

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

    public List<Country> mapToCountryList(List<CountryDTO> countryList) {

       try {
            return objectMapper.readValue(writeValueAsString(countryList), new TypeReference<List<Country>>(){});
        } catch (IOException exception) {
            log.debug("Json exception read value method", exception);
            throw new RuntimeException(exception);
        }
    }

    public Country mapToCountry(CountryDTO countryDTO) {

        try {
            return objectMapper.readValue(writeValueAsString(countryDTO), Country.class);
        } catch (IOException exception) {
            log.debug("Json exception read value method", exception);
            throw new RuntimeException(exception);
        }
    }

    public List<CountryDTO> mapStringToCountryList(String jsonString) {

        try {
            return objectMapper.readValue(jsonString,new TypeReference<List<CountryDTO>>(){});
        } catch (IOException exception) {
            log.debug("Json exception read value method", exception);
            throw new RuntimeException(exception);
        }
    }

    public Manufacturer mapToManufacturerDTO(ManufacturerDTO manufacturerDTO) {

        try {
            return objectMapper.readValue(writeValueAsString(manufacturerDTO), Manufacturer.class);
        } catch (IOException exception) {
            log.debug("Json exception read value method", exception);
            throw new RuntimeException(exception);
        }
    }

    public List<Manufacturer> mapToManufacturerDTO(String manufacturerList) {

        try {
            return objectMapper.readValue(manufacturerList, new TypeReference<List<Manufacturer>>(){});
        } catch (IOException exception) {
            log.debug("Json exception read value method", exception);
            throw new RuntimeException(exception);
        }
    }

    public CountryDTO mapStringToCountry(String country) {

        try {
            return objectMapper.readValue(country, CountryDTO.class);
        } catch (IOException exception) {
            log.debug("Json exception read value method", exception);
            throw new RuntimeException(exception);
        }
    }

    public ManufacturerDTO mapToStringManufacturerDTO(String manufacturer) {

        try {
            return objectMapper.readValue(manufacturer, ManufacturerDTO.class);
        } catch (IOException exception) {
            log.debug("Json exception read value method", exception);
            throw new RuntimeException(exception);
        }
    }
}
