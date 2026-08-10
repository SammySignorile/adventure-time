package org.example.adventuretime.exception;

/** Indicates an invalid or missing startup configuration. */
public class ConfigurationException extends AdventureTimeException {
    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
