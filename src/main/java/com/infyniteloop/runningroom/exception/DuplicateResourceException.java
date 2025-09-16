package com.infyniteloop.runningroom.exception;

/**
 * Custom exception for duplicate resource errors.
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}