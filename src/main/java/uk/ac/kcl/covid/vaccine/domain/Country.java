package uk.ac.kcl.covid.vaccine.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(value = "vaccination")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {

    @JsonProperty("country")
    @Field("country")
    public String country;

    @JsonProperty("iso_code")
    @Field("iso_code")
    public String isoCode;

    @JsonProperty("data")
    @Field("data")
    public List<Vaccine> data;

}

