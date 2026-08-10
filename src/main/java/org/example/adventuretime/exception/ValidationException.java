package org.example.adventuretime.exception;

/** Indicates invalid data supplied by a user-facing controller. */
public class ValidationException extends AdventureTimeException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
