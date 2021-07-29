package ru.proskyryakov.cbrcursondateadapter.adapter.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.proskyryakov.cbrcursondateadapter.adapter.mappers.CursMapper;
import ru.proskyryakov.cbrcursondateadapter.cbr.CbrClient;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CursOnDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

@Service
@RequiredArgsConstructor
public class CursService {
    private final CbrClient client;
    private final CursMapper cursMapper;

    public CursOnDate getCursByCode(String code) {
        var currentDate = new GregorianCalendar();
        return getCursByCodeAndDate(code, currentDate);
    }

    @SneakyThrows
    public CursOnDate getCursByCodeAndDate(String code, String strDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        var calendar = new GregorianCalendar();
        calendar.setTime(df.parse(strDate));
        return getCursByCodeAndDate(code, calendar);
    }

    public CursOnDate getCursByCodeAndDate(String code, GregorianCalendar calendar) {
        var curses = client.getValuteCursOnDate(calendar);
        var curse = curses.stream()
                .filter(c -> c.getVchCode().equalsIgnoreCase(code))
                .findAny().orElse(null);

        if (curse == null) return null;
        return cursMapper.toCursOnDate(curse, calendar);
    }
}
