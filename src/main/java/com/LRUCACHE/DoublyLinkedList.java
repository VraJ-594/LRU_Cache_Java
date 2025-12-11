package com.LRUCACHE;

// Minimal doubly linked list implementation for LRU ordering. Single responsibility: manage ordering operations.
class DoublyLinkedList<K, V> {
    private CacheEntry<K, V> head; // most recently used
    private CacheEntry<K, V> tail; // least recently used
    private int size = 0;

    int size() {
        return size;
    }

    //  Move node to head (mark as most recently used).
    void moveToHead(CacheEntry<K, V> node) {
        if (node == head) return;
        remove(node);
        addFirst(node);
    }

    // Add new node to head.
    void addFirst(CacheEntry<K, V> node) {
        node.prev = null;
        node.next = head;
        if (head != null) head.prev = node;
        head = node;
        if (tail == null) tail = head;
        size++;
    }

    // Remove tail node (least recently used). Returns removed node or null.
    CacheEntry<K, V> removeTail() {
        if (tail == null) return null;
        CacheEntry<K, V> removed = tail;
        remove(removed);
        return removed;
    }

    // Remove an arbitrary node from list.
    void remove(CacheEntry<K, V> node) {
        if (node == null) return;
        CacheEntry<K, V> p = node.prev;
        CacheEntry<K, V> n = node.next;

        if (p != null) p.next = n;
        else head = n;

        if (n != null) n.prev = p;
        else tail = p;

        node.prev = null;
        node.next = null;
        size--;
        if (size < 0) size = 0;
    }

    // Clear the list.
    void clear() {
        head = null;
        tail = null;
        size = 0;
    }
}
