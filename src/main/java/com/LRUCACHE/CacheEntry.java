package com.LRUCACHE;

/**
 * Node used by doubly linked list and cache. Package-private to restrict modification.
 */
class CacheEntry<K, V> {
    final K key;
    V value;
    CacheEntry<K, V> prev;
    CacheEntry<K, V> next;

    CacheEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }
}