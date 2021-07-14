package uk.ac.kcl.covid.vaccine.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(value = "manufacturer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manufacturer {

    @JsonProperty("location")
    @Field("location")
    private String location;

    @JsonProperty("iso_code")
    @Field("iso_code")
    public String isoCode;

    @JsonProperty("vaccineCompanies")
    @Field("vaccineCompanies")
    public List<VaccineCompany> vaccineCompanies;

}
