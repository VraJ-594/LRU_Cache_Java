package com.LRUCACHE;

import com.LRUCACHE.exceptions.CacheException;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    @Test
    void putAndGet_shouldReturnValue() throws CacheException {
        Cache<String, Integer> cache = new LRUCache<>(3);
        cache.put("a", 1);
        Optional<Integer> v = cache.get("a");
        assertTrue(v.isPresent());
        assertEquals(1, v.get());
        assertEquals(1, cache.size());
    }

    @Test
    void evictLeastRecentlyUsed_whenCapacityExceeded() throws CacheException {
        Cache<String, Integer> cache = new LRUCache<>(2);
        cache.put("a", 1);
        cache.put("b", 2);
        // access 'a' to make it recently used
        cache.get("a");
        // add 'c' -> should evict 'b'
        cache.put("c", 3);

        assertTrue(cache.get("a").isPresent());
        assertFalse(cache.get("b").isPresent());
        assertTrue(cache.get("c").isPresent());
        assertEquals(2, cache.size());
    }

    @Test
    void updateExistingKey_movesToHead_andUpdatesValue() throws CacheException {
        Cache<String, String> cache = new LRUCache<>(2);
        cache.put("x", "X");
        cache.put("y", "Y");
        // update x
        cache.put("x", "X2");
        // insert z should evict y
        cache.put("z", "Z");
        assertTrue(cache.get("x").isPresent());
        assertEquals("X2", cache.get("x").get());
        assertFalse(cache.get("y").isPresent());
        assertTrue(cache.get("z").isPresent());
    }

    @Test
    void capacityOne_behaviour() throws CacheException {
        Cache<Integer, Integer> cache = new LRUCache<>(1);
        cache.put(1, 1);
        assertTrue(cache.get(1).isPresent());
        cache.put(2, 2);
        assertFalse(cache.get(1).isPresent());
        assertTrue(cache.get(2).isPresent());
    }

    @Test
    void removeAndClear() throws CacheException {
        Cache<String, Integer> cache = new LRUCache<>(3);
        cache.put("a", 1);
        cache.put("b", 2);
        Optional<Integer> removed = cache.remove("a");
        assertTrue(removed.isPresent() && removed.get() == 1);
        assertFalse(cache.get("a").isPresent());
        cache.clear();
        assertEquals(0, cache.size());
    }

    @Test
    void nullKey_throwsException() {
        Cache<String, Integer> cache = new LRUCache<>(2);
        assertThrows(NullPointerException.class, () -> cache.put(null, 1));
        assertThrows(NullPointerException.class, () -> cache.get(null));
    }

    @Test
    void testCapacity() {
        Cache<String, Integer> cache = new LRUCache<>(2);
        assertEquals(2,cache.capacity());
    }
}
