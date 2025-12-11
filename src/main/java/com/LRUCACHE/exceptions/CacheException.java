package com.LRUCACHE.exceptions;

// Application-level cache exception to centralize errors.
public class CacheException extends Exception {
    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
}
