package ru.proskyryakov.cbrcursondateadapter.scheduler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CursOnDate;
import ru.proskyryakov.cbrcursondateadapter.adapter.services.CursHistoryService;
import ru.proskyryakov.cbrcursondateadapter.adapter.services.CursService;
import ru.proskyryakov.cbrcursondateadapter.db.entities.History;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Interval;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Valute;
import ru.proskyryakov.cbrcursondateadapter.db.repositories.HistoryRepository;
import ru.proskyryakov.cbrcursondateadapter.db.repositories.IntervalRepository;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    private final CursHistoryService cursHistoryService;
    private final CursService cursService;
    private final IntervalRepository intervalRepository;
    private final HistoryRepository historyRepository;

    @Value("${scheduler.interval-coefficient}")
    private long intervalCoeff;

    @Scheduled(fixedDelayString = "${scheduler.fixedDelay}", initialDelayString = "${scheduler.initialDelay}")
    public void start() {
        log.info("Scheduled work");
        List<History> histories = historyRepository.findAllLastEntry();

//        Date date = new Date();
        var calendar = new GregorianCalendar();
        List<Valute> tracedValutes = getTrackedValute(histories, calendar.getTime());

        tracedValutes.addAll(
                intervalRepository.findNewIntervals().stream()
                        .map(Interval::getValute)
                        .collect(Collectors.toList())
        );

        if (!tracedValutes.isEmpty()) {
           addToHistory(tracedValutes, calendar);
        }
    }

    private List<Valute> getTrackedValute(List<History> histories, Date currentDate) {
        return histories.stream().filter(h ->
                currentDate.after(new Date(h.getDatetime().getTime() + h.getInterval().getInterval() * intervalCoeff)))
                .map(h -> h.getInterval().getValute())
                .collect(Collectors.toList());
    }

    private void addToHistory(List<Valute> tracedValutes, GregorianCalendar currentDate) {
        List<CursOnDate> cursOnDateList = cursService.getCursByCodesAndDate(
                tracedValutes.stream().map(Valute::getCode).collect(Collectors.toList()),
                currentDate
        );

        Map<String, Interval> intervals = intervalRepository.findAllByIsActualTrue().stream()
                .collect(Collectors.toMap(i -> i.getValute().getCode(), i -> i));

        for(CursOnDate curs: cursOnDateList) {
            History h = new History();
            h.setCurse(curs.getCurs());
            h.setDatetime(currentDate.getTime());
            h.setInterval(intervals.get(curs.getCode()));
            historyRepository.save(h);
        }

    }

}
