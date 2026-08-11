package org.example.adventuretime.dao.db;

import org.example.adventuretime.exception.PersistenceException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/** Inizializza MySQL usando lo script incluso nelle risorse. */
final class DatabaseInitializer {

    private static final String SCRIPT_RESOURCE =
            "/database/adventuretime.sql";

    private DatabaseInitializer() {
    }

    static void initialize(Connection connection) throws PersistenceException {
        if (isInitialized(connection)) {
            return;
        }

        String script = readScript();
        try {
            executeStatements(connection, script);
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Impossibile inizializzare il database MySQL.", e);
        }
    }

    private static boolean isInitialized(Connection connection) {
        String sql = "SELECT schema_version FROM app_metadata WHERE id = 1";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            return resultSet.next();
        } catch (SQLException ignored) {
            return false;
        }
    }

    private static String readScript() throws PersistenceException {
        try (InputStream stream = DatabaseInitializer.class
                .getResourceAsStream(SCRIPT_RESOURCE)) {
            if (stream == null) {
                throw new PersistenceException(
                        "Script del database non trovato: " + SCRIPT_RESOURCE);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenceException(
                    "Impossibile leggere lo script del database.", e);
        }
    }

    private static void executeStatements(
            Connection connection,
            String script
    ) throws SQLException {
        String sqlWithoutComments = script.lines()
                .filter(line -> !line.stripLeading().startsWith("--"))
                .collect(Collectors.joining("\n"));

        try (Statement statement = connection.createStatement()) {
            for (String sql : sqlWithoutComments.split(";")) {
                if (!sql.isBlank()) {
                    statement.execute(sql.trim());
                }
            }
        }
    }
}
