package com.LRUCACHE;

import com.LRUCACHE.exceptions.CacheException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LRUCache implementation:
 * - Generic K, V
 * - Thread-safe via a ReentrantLock (simple and clear)
 * - Uses HashMap + DoublyLinkedList to achieve O(1) put/get/remove
 *
 * Principles applied:
 * - Single Responsibility: this class orchestrates the cache behavior.
 * - Dependency Inversion: depends on abstractions (Cache interface).
 * - KISS / YAGNI: only LRU implemented; easy to extend later.
 * - DRY: helper methods avoid repetition.
 */
public class LRUCache<K, V> implements Cache<K, V> {
    private final int capacity;
    private final Map<K, CacheEntry<K, V>> map;
    private final DoublyLinkedList<K, V> list;
    private final ReentrantLock lock = new ReentrantLock(true);

    public LRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be > 0");
        }
        this.capacity = capacity;
        this.map = new HashMap<>(Math.max(16, capacity * 2));
        this.list = new DoublyLinkedList<>();
    }

    @Override
    public void put(K key, V value) throws CacheException {
        Objects.requireNonNull(key, "key is null");
        try {
            lock.lock();
            CacheEntry<K, V> node = map.get(key);
            if (node != null) {
                // update value and move to head
                node.value = value;
                list.moveToHead(node);
                return;
            }
            // new entry
            if (map.size() >= capacity) {
                evict();
            }
            CacheEntry<K, V> newNode = new CacheEntry<>(key, value);
            list.addFirst(newNode);
            map.put(key, newNode);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Optional<V> get(K key) throws CacheException {
        Objects.requireNonNull(key, "key is null");
        try {
            lock.lock();
            CacheEntry<K, V> node = map.get(key);
            if (node == null) return Optional.empty();
            // mark as recently used
            list.moveToHead(node);
            return Optional.ofNullable(node.value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Optional<V> remove(K key) throws CacheException {
        Objects.requireNonNull(key, "key is null");
        try {
            lock.lock();
            CacheEntry<K, V> node = map.remove(key);
            if (node == null) return Optional.empty();
            list.remove(node);
            return Optional.ofNullable(node.value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        try {
            lock.lock();
            return map.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            map.clear();
            list.clear();
        } finally {
            lock.unlock();
        }
    }

    private void evict() {
        CacheEntry<K, V> tail = list.removeTail();
        if (tail != null) {
            map.remove(tail.key);
        }
    }

    @Override
    public String toString() {
        // lightweight representation for debugging (not exposing internals).
        return "LRUCache{capacity=" + capacity + ", size=" + size() + "}";
    }
}
