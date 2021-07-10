package uk.ac.kcl.covid.vaccine.data_transfer;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "date",
        "total_vaccinations",
        "people_vaccinated",
        "total_vaccinations_per_hundred",
        "people_vaccinated_per_hundred",
        "people_fully_vaccinated",
        "daily_vaccinations",
        "people_fully_vaccinated_per_hundred",
        "daily_vaccinations_per_million"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineDTO {

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date date;
    @JsonProperty("total_vaccinations")
    public Long totalVaccinations;
    @JsonProperty("people_vaccinated")
    public Long peopleVaccinated;
    @JsonProperty("total_vaccinations_per_hundred")
    public Double totalVaccinationsPerHundred;
    @JsonProperty("people_vaccinated_per_hundred")
    public Double peopleVaccinatedPerHundred;
    @JsonProperty("people_fully_vaccinated")
    public Integer peopleFullyVaccinated;
    @JsonProperty("daily_vaccinations")
    public Integer dailyVaccinations;
    @JsonProperty("people_fully_vaccinated_per_hundred")
    public Double peopleFullyVaccinatedPerHundred;
    @JsonProperty("daily_vaccinations_per_million")
    public Integer dailyVaccinationsPerMillion;

}