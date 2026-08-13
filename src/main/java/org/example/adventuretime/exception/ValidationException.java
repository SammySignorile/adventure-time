package org.example.adventuretime.exception;

public class ValidationException extends AdventureTimeException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
