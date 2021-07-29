package ru.proskyryakov.cbrcursondateadapter.adapter.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.proskyryakov.cbrcursondateadapter.adapter.mappers.CursMapper;
import ru.proskyryakov.cbrcursondateadapter.cbr.CbrClient;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CursOnDate;

import java.util.GregorianCalendar;

@Service
@RequiredArgsConstructor
public class CursService {
    private final CbrClient client;
    private final CursMapper cursMapper;

    public CursOnDate getCursByCode(String code) {
        var currentDate = new GregorianCalendar();
        var curses = client.getValuteCursOnDate(currentDate);
        var curse = curses.stream()
                .filter(c -> c.getVchCode().equalsIgnoreCase(code))
                .findAny().orElse(null);
        if (curse == null) return null;
        return cursMapper.toCursOnDate(curse, currentDate);
    }

}
