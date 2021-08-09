package ru.proskyryakov.cbrcursondateadapter.adapter.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.proskuryakov.cbrcursondateadapter.cbr.wsdl.ValuteData;
import ru.proskyryakov.cbrcursondateadapter.adapter.exceptions.NotFoundException;
import ru.proskyryakov.cbrcursondateadapter.adapter.mappers.CursMapper;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CursOnDate;
import ru.proskyryakov.cbrcursondateadapter.cbr.CbrClient;

import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;

@Service
@RequiredArgsConstructor
public class ValuteService {

    private static final Logger log = LoggerFactory.getLogger(CursService.class);

    private final CbrClient client;
    private final CursMapper cursMapper;

    @SneakyThrows
    @Cacheable(value = "curs-cache", key = "#key")
    public CursOnDate getCursByCodeAndDate(String code, GregorianCalendar calendar, String key) {

        log.info(
                "Request with code {} on date {}",
                code.toUpperCase(),
                calendar.toZonedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        try {
            var curse = getCursByCodeAndDateInner(code, calendar);
            if (curse == null) return null;
            return cursMapper.toCursOnDate(curse, calendar);
        } catch (NullPointerException e) {
            throw new NotFoundException(String.format("Curs by %s code not found", code));
        }
    }

    public ValuteData.ValuteCursOnDate getCursByCodeAndDateInner(String code, GregorianCalendar calendar) {
        return client.getValuteCursOnDate(calendar)
                .stream()
                .filter(c -> c.getVchCode().equalsIgnoreCase(code))
                .findAny().orElse(null);
    }

}
