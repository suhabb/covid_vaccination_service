package uk.ac.kcl.covid.vaccine.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vaccine {

    @JsonProperty("date")
    @Field("date")
    public Date date;

    @JsonProperty("total_vaccinations")
    @Field("total_vaccinations")
    public Long totalVaccinations;

    @JsonProperty("people_vaccinated")
    @Field("people_vaccinated")
    public Long peopleVaccinated;

    @JsonProperty("total_vaccinations_per_hundred")
    @Field("total_vaccinations_per_hundred")
    public Double totalVaccinationsPerHundred;

    @JsonProperty("people_vaccinated_per_hundred")
    @Field("people_vaccinated_per_hundred")
    public Double peopleVaccinatedPerHundred;

    @JsonProperty("people_fully_vaccinated")
    @Field("people_fully_vaccinated")
    public Long peopleFullyVaccinated;

    @JsonProperty("daily_vaccinations")
    @Field("daily_vaccinations")
    public Long dailyVaccinations;

    @JsonProperty("people_fully_vaccinated_per_hundred")
    @Field("people_fully_vaccinated_per_hundred")
    public Double peopleFullyVaccinatedPerHundred;

    @JsonProperty("daily_vaccinations_per_million")
    @Field("daily_vaccinations_per_million")
    public Long dailyVaccinationsPerMillion;

}