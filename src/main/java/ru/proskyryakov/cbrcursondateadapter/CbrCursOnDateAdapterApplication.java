package ru.proskyryakov.cbrcursondateadapter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.proskuryakov.cbrcursondateadapter.cbr.wsdl.GetCursOnDateXMLResponse;
import ru.proskyryakov.cbrcursondateadapter.cbr.CbrClient;

import java.util.Calendar;
import java.util.GregorianCalendar;

@SpringBootApplication
public class CbrCursOnDateAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CbrCursOnDateAdapterApplication.class, args);
    }

    @Bean
    CommandLineRunner lookup(CbrClient client) {
        return args -> {
            String country = "Spain";

            if (args.length > 0) {
                country = args[0];
            }
            GetCursOnDateXMLResponse response = client.getCursOnDateXMLResponse(new GregorianCalendar(2021, Calendar.JULY, 27));
            System.err.println(response.getGetCursOnDateXMLResult().getContent());
        };
    }

}
