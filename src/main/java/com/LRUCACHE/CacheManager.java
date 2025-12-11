package com.LRUCACHE;

import com.LRUCACHE.exceptions.CacheException;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple manager that allows creation and retrieval of named caches.
 * Useful in a larger system where multiple caches are needed.
 * Keeps management responsibilities separate from cache logic.
 */
public class CacheManager {
    private final Map<String, Cache<?, ?>> caches = new ConcurrentHashMap<>();

    public <K, V> Cache<K, V> createNamedCache(String name, EvictionPolicy policy, int capacity) throws CacheException {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("name required");
        Cache<K, V> cache = CacheFactory.createCache(policy, capacity);
        caches.put(name, cache);
        @SuppressWarnings("unchecked")
        Cache<K, V> typed = (Cache<K, V>) cache;
        return typed;
    }

    public Cache<?, ?> getCache(String name) {
        return caches.get(name);
    }

    public Map<String, Cache<?, ?>> listCaches() {
        return Collections.unmodifiableMap(caches);
    }
}
