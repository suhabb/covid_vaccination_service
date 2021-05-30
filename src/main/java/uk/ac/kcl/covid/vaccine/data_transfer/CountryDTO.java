package uk.ac.kcl.covid.vaccine.data_transfer;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "country",
        "iso_code",
        "json"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO {

    @JsonProperty("country")
    public String country;

    @JsonProperty("iso_code")
    public String isoCode;

    @JsonProperty("data")
    public List<VaccineDTO> data;

}

