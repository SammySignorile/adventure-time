package org.example.adventuretime.exception;

public class AuthorizationException extends AdventureTimeException {
    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
