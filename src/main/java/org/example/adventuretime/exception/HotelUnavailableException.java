package org.example.adventuretime.exception;

public class HotelUnavailableException extends AdventureTimeException {
    public HotelUnavailableException(String message) {
        super(message);
    }

    public HotelUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
