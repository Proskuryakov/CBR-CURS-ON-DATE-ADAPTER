package ru.proskyryakov.cbrcursondateadapter.adapter.mappers;

import org.springframework.stereotype.Service;
import ru.proskuryakov.cbrcursondateadapter.cbr.wsdl.ValuteData;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CursOnDate;

import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class CursMapper {

    public CursOnDate toCursOnDate(ValuteData.ValuteCursOnDate valuteCursOnDate, GregorianCalendar currentDate){
        CursOnDate cursOnDate = new CursOnDate();
        cursOnDate.setCurs(valuteCursOnDate.getVcurs());
        cursOnDate.setCode(valuteCursOnDate.getVchCode());
        cursOnDate.setDate(currentDate.getTime());
        return cursOnDate;
    }

}
