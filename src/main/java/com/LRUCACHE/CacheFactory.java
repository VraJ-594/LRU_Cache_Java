package com.LRUCACHE;

import com.LRUCACHE.exceptions.CacheException;

// Tiny factory to create caches. Keeps construction logic in one place (single responsibility and easy to expand).
public final class CacheFactory {
    private CacheFactory() {
    }

    public static <K, V> Cache<K, V> createCache(EvictionPolicy policy, int capacity) throws CacheException {
        if (policy == null) throw new IllegalArgumentException("policy is null");
        switch (policy) {
            case LRU:
                return new LRUCache<>(capacity);
            case FIFO:
                // For YAGNI we don't implement FIFO now — can add new class later.
            default:
                throw new CacheException("Unsupported eviction policy: " + policy);
        }
    }
}
