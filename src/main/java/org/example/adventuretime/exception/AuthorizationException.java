package org.example.adventuretime.exception;

/** Indicates that the logged user does not own the required privilege. */
public class AuthorizationException extends AdventureTimeException {
    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
