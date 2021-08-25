package ru.proskyryakov.cbrcursondateadapter.adapter.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.proskyryakov.cbrcursondateadapter.adapter.exceptions.DateConversionException;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CodeWithDates;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CursOnDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CursService {

    private final ValuteService valuteService;

    public CursOnDate getCursByCode(String code) {
        var currentDate = new GregorianCalendar();
        return valuteService.getCursByCodeAndDate(code, currentDate, genKey(code, currentDate));
    }

    public List<CursOnDate> getCursByDates(CodeWithDates codeWithDates) {
        return codeWithDates.getDates().stream()
                .map(date -> getCursByCodeAndDate(codeWithDates.getCode(), date))
                .collect(Collectors.toList());
    }

    public CursOnDate getCursByCodeAndDate(String code, String strDate) {
        GregorianCalendar calendar = parseDate(strDate);
        return valuteService.getCursByCodeAndDate(code, calendar, genKey(code, calendar));
    }

    private String genKey(String code, GregorianCalendar calendar) {
        return code.toUpperCase() + calendar.toZonedDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public List<CursOnDate> getCursByCodesAndDate(List<String> codes, GregorianCalendar calendar) {
        return codes.stream()
                .map(code -> valuteService.getCursByCodeAndDate(code, calendar, genKey(code, calendar)))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private GregorianCalendar parseDate(String strDate) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(df.parse(strDate));
            return calendar;
        } catch (ParseException e) {
            throw new DateConversionException(String.format("Incorrect date %s", strDate));
        }
    }
}
