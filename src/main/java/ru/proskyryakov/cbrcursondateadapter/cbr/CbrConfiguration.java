package ru.proskyryakov.cbrcursondateadapter.cbr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class CbrConfiguration {

    @Value("${cbr.uri}")
    private String cbrUri;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in
        // pom.xml
        marshaller.setContextPath("ru.proskuryakov.cbrcursondateadapter.cbr.wsdl");
        return marshaller;
    }

    @Bean
    public CbrClient cbrClient(Jaxb2Marshaller marshaller) {
        CbrClient client = new CbrClient();
        client.setDefaultUri(cbrUri);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

}
