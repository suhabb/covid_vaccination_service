package uk.ac.kcl.covid.vaccine.application_service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

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

    @Test
    public void test2(){
        int[][] arrays = new int[4][];

        arrays[0] = new int[] {25, 33, 25, 17};
        arrays[1] = new int[] {10, 23, 30, 37};
        arrays[2] = new int[] {19, 21, 40, 20};
        arrays[3] = new int[] {35, 15, 20, 30};
        Random random = new Random();
        int number = random.nextInt(4);

        int[] selectedArray = arrays[number];
        System.out.println("CountryApplicationServiceTest.test2:"+ number+"---"+selectedArray[3]);
    }
}
