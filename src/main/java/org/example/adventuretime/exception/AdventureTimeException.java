package org.example.adventuretime.exception;

public class AdventureTimeException extends Exception {

    private static final long serialVersionUID = 1L;
    public AdventureTimeException(String message) {
        super(message);
    }

    public AdventureTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
