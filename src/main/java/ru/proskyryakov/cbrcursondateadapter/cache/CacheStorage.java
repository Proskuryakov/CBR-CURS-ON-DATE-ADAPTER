package ru.proskyryakov.cbrcursondateadapter.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CacheStorage<K, V> {

    public static final Long DEFAULT_SAVE_TIME = 86_400L;
    private static final int CONVERT_COEFF = 1000;

    private final ConcurrentMap<K, DateValue<V>> cache;
    private final Long saveTime;

    public CacheStorage() {
        this(DEFAULT_SAVE_TIME);
    }

    public CacheStorage(Long saveTime) {
        cache = new ConcurrentHashMap<>();
        this.saveTime = saveTime * CONVERT_COEFF;

        new CacheCleaner<>(cache);
    }

    public void add(K key, V value) {
        cache.put(key, new DateValue<V>(value, saveTime));
    }

    public V get(K key) {
        var dateValue = cache.get(key);
        if(dateValue == null) return null;
        return dateValue.getValue();
    }


}
