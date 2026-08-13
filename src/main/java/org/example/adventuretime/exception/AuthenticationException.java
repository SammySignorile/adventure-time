package org.example.adventuretime.exception;

public class AuthenticationException extends AdventureTimeException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
