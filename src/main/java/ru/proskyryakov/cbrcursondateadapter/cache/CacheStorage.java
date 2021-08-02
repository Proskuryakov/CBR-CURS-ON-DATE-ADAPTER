package ru.proskyryakov.cbrcursondateadapter.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheStorage<K, V> {

    private final Map<K, V> cache;

    public CacheStorage() {
        cache = new HashMap<>();
    }

    public void add(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.get(key);
    }

}
