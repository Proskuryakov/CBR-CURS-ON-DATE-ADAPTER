package ru.proskyryakov.cbrcursondateadapter.cbr;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class CbrConfiguration {

    @Value("${cbr.uri}")
    private String cbrUri;
    @Value("${cbr.wsdl.GenerationPackage}")
    private String generationPackage;

    @SneakyThrows
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(generationPackage);
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
