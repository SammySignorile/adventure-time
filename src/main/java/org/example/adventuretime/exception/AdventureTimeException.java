package org.example.adventuretime.exception;

/**
 * Base checked exception for errors that can be translated into a clear message
 * for the final user. Technical exceptions are wrapped at the layer in which
 * they occur instead of leaking JDBC or I/O details to the UI.
 */
public class AdventureTimeException extends Exception {

    private static final long serialVersionUID = 1L;
    public AdventureTimeException(String message) {
        super(message);
    }

    public AdventureTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
