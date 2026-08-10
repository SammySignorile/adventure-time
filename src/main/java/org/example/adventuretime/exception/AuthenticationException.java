package org.example.adventuretime.exception;

/** Indicates invalid credentials or an unavailable account. */
public class AuthenticationException extends AdventureTimeException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
