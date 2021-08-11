package ru.proskyryakov.cbrcursondateadapter.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.proskyryakov.cbrcursondateadapter.adapter.services.CursHistoryService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final CursHistoryService cursHistoryService;

    @Scheduled(fixedDelayString = "${scheduler.fixedDelay}", initialDelayString = "${scheduler.initialDelay}")
    public void start() {
        System.out.println("Работаю с задержкой " + LocalDateTime.now());
    }

}
