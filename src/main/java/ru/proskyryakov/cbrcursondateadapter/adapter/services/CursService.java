package ru.proskyryakov.cbrcursondateadapter.adapter.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import ru.proskuryakov.cbrcursondateadapter.cbr.wsdl.ValuteData;
import ru.proskyryakov.cbrcursondateadapter.adapter.exceptions.DateConversionException;
import ru.proskyryakov.cbrcursondateadapter.adapter.exceptions.NotFoundException;
import ru.proskyryakov.cbrcursondateadapter.adapter.mappers.CursMapper;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CodeWithDates;
import ru.proskyryakov.cbrcursondateadapter.cbr.CbrClient;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CursOnDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableCaching
public class CursService {

    private static final Logger log = LoggerFactory.getLogger(CursService.class);

    private final CbrClient client;
    private final CursMapper cursMapper;

    public CursOnDate getCursByCode(String code) {
        var currentDate = new GregorianCalendar();
        return getCursByCodeAndDate(code, currentDate);
    }

    @SneakyThrows
    public CursOnDate getCursByCodeAndDate(String code, String strDate) {
        GregorianCalendar calendar;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            calendar = new GregorianCalendar();
            calendar.setTime(df.parse(strDate));
            calendar.add(Calendar.DATE, 1);
        } catch (ParseException e) {
            throw new DateConversionException(String.format("Incorrect date %s", strDate));
        }
        return getCursByCodeAndDate(code, calendar);
    }

    @SneakyThrows
    public CursOnDate getCursByCodeAndDate(String code, GregorianCalendar calendar) {
        var key = genKey(code, calendar);

        log.info(
                "Request with code {} on date {}",
                code.toUpperCase(),
                calendar.toZonedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        try {
            var curse = getCursByCodeAndDateInner(code, calendar, key);
            if (curse == null) return null;
            return cursMapper.toCursOnDate(curse, calendar);
        } catch (NullPointerException e) {
            throw new NotFoundException(String.format("Curs by %s code not found", code));
        }
    }

    @Cacheable(value = "curs-cache", key = "#key")
    public ValuteData.ValuteCursOnDate getCursByCodeAndDateInner(String code, GregorianCalendar calendar, String key) {
        return client.getValuteCursOnDate(calendar)
                .stream()
                .filter(c -> c.getVchCode().equalsIgnoreCase(code))
                .findAny().orElse(null);
    }


    public List<CursOnDate> getCursByDates(CodeWithDates codeWithDates) {
        return codeWithDates.getDates().stream()
                .map(date -> getCursByCodeAndDate(codeWithDates.getCode(), date))
                .collect(Collectors.toList());
    }

    private String genKey(String code, GregorianCalendar calendar) {
        return code.toUpperCase() + calendar.toZonedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
