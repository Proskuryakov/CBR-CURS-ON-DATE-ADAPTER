package ru.proskyryakov.cbrcursondateadapter.scheduler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CursOnDate;
import ru.proskyryakov.cbrcursondateadapter.adapter.services.CursService;
import ru.proskyryakov.cbrcursondateadapter.db.entities.History;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Interval;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Valute;
import ru.proskyryakov.cbrcursondateadapter.db.repositories.HistoryRepository;
import ru.proskyryakov.cbrcursondateadapter.db.repositories.IntervalRepository;
import ru.proskyryakov.cbrcursondateadapter.rabbit.ChangeCursModel;
import ru.proskyryakov.cbrcursondateadapter.rabbit.ChangeDirection;
import ru.proskyryakov.cbrcursondateadapter.rabbit.RabbitService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    private final RabbitService rabbitService;
    private final CursService cursService;
    private final IntervalRepository intervalRepository;
    private final HistoryRepository historyRepository;

    @Value("${scheduler.interval-coefficient}")
    private long intervalCoeff;

    @Scheduled(fixedDelayString = "${scheduler.fixedDelay}", initialDelayString = "${scheduler.initialDelay}")
    public void start() {
        log.info("Scheduled work");
        List<History> histories = historyRepository.findAllLastEntry();

        var calendar = new GregorianCalendar();
        List<Valute> tracedValutes = getTrackedValute(histories, calendar.getTime());

        tracedValutes.addAll(
                intervalRepository.findNewIntervals().stream()
                        .map(Interval::getValute)
                        .collect(Collectors.toList())
        );

        if (!tracedValutes.isEmpty()) {
            List<CursOnDate> cursOnDateList = getNewCurses(tracedValutes, calendar);
            addToHistory(cursOnDateList, calendar);
            addToQueue(histories, cursOnDateList, calendar);
        }
    }

    private List<Valute> getTrackedValute(List<History> histories, Date currentDate) {
        return histories.stream().filter(h ->
                currentDate.after(new Date(h.getDatetime().getTime() + h.getInterval().getInterval() * intervalCoeff)))
                .map(h -> h.getInterval().getValute())
                .collect(Collectors.toList());
    }

    private List<CursOnDate> getNewCurses(List<Valute> tracedValutes, GregorianCalendar currentDate) {
        return cursService.getCursByCodesAndDate(
                tracedValutes.stream().map(Valute::getCode).collect(Collectors.toList()),
                currentDate
        );
    }

    private void addToHistory(List<CursOnDate> cursOnDateList, GregorianCalendar currentDate) {
        Map<String, Interval> intervals = intervalRepository.findAllByIsActualTrue().stream()
                .collect(Collectors.toMap(i -> i.getValute().getCode(), i -> i));

        for (CursOnDate curs : cursOnDateList) {
            History h = new History();
            h.setCurse(curs.getCurs());
            h.setDatetime(currentDate.getTime());
            h.setInterval(intervals.get(curs.getCode()));
            historyRepository.save(h);
        }

    }

    private void addToQueue(List<History> histories, List<CursOnDate> cursOnDateList, GregorianCalendar currentDate) {
        Map<String, BigDecimal> code2curs = histories.stream()
                .collect(Collectors.toMap(h -> h.getInterval().getValute().getCode(), History::getCurse));

        for (CursOnDate curs : cursOnDateList) {
            int compare = curs.getCurs().compareTo(code2curs.get(curs.getCode()));
            if (compare != 0) {
                BigDecimal diff = curs.getCurs().subtract(code2curs.get(curs.getCode())).abs();

                ChangeCursModel changeCursModel = new ChangeCursModel();

                changeCursModel.setDifference(diff);
                changeCursModel.setDirection(compare > 0 ? ChangeDirection.INC : ChangeDirection.DEC);
                changeCursModel.setDatetime(currentDate.getTime());

                rabbitService.send(changeCursModel);
            }
        }
    }

}
