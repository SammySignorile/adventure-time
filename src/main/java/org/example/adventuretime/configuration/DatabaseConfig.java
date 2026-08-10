package org.example.adventuretime.configuration;

/** Parametri necessari per aprire la connessione JDBC. */
public record DatabaseConfig(String url, String user, String password) {
}
