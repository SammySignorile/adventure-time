package org.example.adventuretime.exception;

/** Wraps errors produced by the selected persistence mechanism. */
public class PersistenceException extends AdventureTimeException {
    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
