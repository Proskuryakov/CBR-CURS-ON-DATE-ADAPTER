package ru.proskyryakov.cbrcursondateadapter.cache;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CacheCleaner<K, V>{

    private final static long DELAY_TIME = 5;

    private final ConcurrentMap<K, DateValue<V>> cache;

    public CacheCleaner(ConcurrentMap<K, DateValue<V>> cache) {
        this.cache = cache;
        startThread();
    }

    private void startThread(){
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            cache.entrySet().removeIf(entry -> !entry.getValue().isLive());
        };
        scheduledExecutorService.scheduleWithFixedDelay(task, 1, DELAY_TIME, TimeUnit.SECONDS);
    }

}