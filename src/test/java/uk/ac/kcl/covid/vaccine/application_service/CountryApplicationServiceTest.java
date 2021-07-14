package uk.ac.kcl.covid.vaccine.application_service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Locale;

@Slf4j
public class CountryApplicationServiceTest {


    @Test
    public void test(){
        HashMap<String, String> mapOfIso3Countries = new HashMap<String, String>();
        String[] countryCodes = Locale.getISOCountries();
        for (String cc : countryCodes) {
            // country name , country code map
            Locale locale = new Locale("", cc);
            mapOfIso3Countries.put(new Locale("", cc).getDisplayCountry(), locale.getISO3Country()
                    .toUpperCase());
        }
    }
}
