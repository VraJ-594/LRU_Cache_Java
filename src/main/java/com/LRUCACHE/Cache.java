package com.LRUCACHE;

import com.LRUCACHE.exceptions.CacheException;
import java.util.Optional;


public interface Cache<K, V> {
        void put(K key, V value) throws CacheException;

        Optional<V> get(K key) throws CacheException;

        Optional<V> remove(K key) throws CacheException;

        int size();

        int capacity();

        void clear();
}
