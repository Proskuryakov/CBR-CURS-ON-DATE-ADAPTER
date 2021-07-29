package ru.proskyryakov.cbrcursondateadapter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.proskuryakov.cbrcursondateadapter.cbr.wsdl.GetCursOnDateXMLResponse;
import ru.proskuryakov.cbrcursondateadapter.cbr.wsdl.ValuteData;
import ru.proskyryakov.cbrcursondateadapter.cbr.CbrClient;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.stream.Collectors;

@SpringBootApplication
public class CbrCursOnDateAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CbrCursOnDateAdapterApplication.class, args);
    }

    @Bean
    CommandLineRunner lookup(CbrClient client) {
        return args -> {

            var date = new GregorianCalendar(2021, Calendar.JULY, 27);
            var valute = client.getValuteCursOnDate(date);
            valute.stream().map(this::valuteCursOnDateToString).forEach(System.out::println);

        };
    }

    private String valuteCursOnDateToString (ValuteData.ValuteCursOnDate valuteCursOnDate){
        return valuteCursOnDate.getVname().trim() + " " + valuteCursOnDate.getVchCode() + " " + valuteCursOnDate.getVcurs();
    }

}
