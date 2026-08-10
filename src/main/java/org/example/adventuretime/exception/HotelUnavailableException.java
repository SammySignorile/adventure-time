package org.example.adventuretime.exception;

/** Indicates that the selected room is no longer available. */
public class HotelUnavailableException extends AdventureTimeException {
    public HotelUnavailableException(String message) {
        super(message);
    }

    public HotelUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
