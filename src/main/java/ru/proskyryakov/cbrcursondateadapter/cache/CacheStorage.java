package ru.proskyryakov.cbrcursondateadapter.cache;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CacheStorage<K, V> {

    public static final Long DEFAULT_SAVE_TIME = 86_400L;

    private final ConcurrentMap<K, DateValue<V>> cache;
    private final Long saveTime;

    public CacheStorage() {
        this(DEFAULT_SAVE_TIME);
    }

    public CacheStorage(Long saveTime) {
        cache = new ConcurrentHashMap<>();
        this.saveTime = saveTime;
    }

    public void add(K key, V value) {
        cache.put(key, new DateValue<>(value, saveTime));
    }

    public V get(K key) {
        var dateValue = cache.get(key);
        if(dateValue == null) return null;
        return dateValue.value;
    }

    private static class DateValue<V> {
        public V value;
        public Date date;

        public DateValue(V value, Long saveTime) {
            this.value = value;
            date = new Date();
            date.setTime(date.getTime() + saveTime);
        }

        public boolean isLive() {
            return date.getTime() > new Date().getTime();
        }
    }

}
