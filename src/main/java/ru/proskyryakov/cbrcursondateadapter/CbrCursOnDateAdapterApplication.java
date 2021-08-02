package ru.proskyryakov.cbrcursondateadapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.proskyryakov.cbrcursondateadapter.cache.CacheStorage;

@SpringBootApplication
public class CbrCursOnDateAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CbrCursOnDateAdapterApplication.class, args);
    }

    @Bean
    public <K, V> CacheStorage<K, V> cacheStorage() {
        CacheStorage<K, V> cacheStorage = new CacheStorage<>();
        return cacheStorage;
    }

}
