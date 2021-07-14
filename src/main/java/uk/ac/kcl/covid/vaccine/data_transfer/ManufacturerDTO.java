package uk.ac.kcl.covid.vaccine.data_transfer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManufacturerDTO {

    @JsonProperty("location")
    @Field("location")
    private String location;

    @JsonProperty("iso_code")
    @Field("iso_code")
    public String isoCode;

    @JsonProperty("vaccineCompanies")
    @Field("vaccineCompanies")
    public List<VaccineCompanyDTO> vaccineCompanies;
}
