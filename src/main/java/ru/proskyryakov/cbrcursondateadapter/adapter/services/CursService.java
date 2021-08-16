package ru.proskyryakov.cbrcursondateadapter.adapter.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Date;
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
        return valuteService.getCursByCodeAndDate(code, calendar, genKey(code, calendar));
    }

    private String genKey(String code, GregorianCalendar calendar) {
        return code.toUpperCase() + calendar.toZonedDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public List<CursOnDate> getCursByCodesAndDate(List<String> codes, Date date) {
        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return codes.stream().map(code -> getCursByCodeAndDate(code, strDate)).collect(Collectors.toList());
    }
}
