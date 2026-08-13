package org.example.adventuretime.dao.db;

import org.example.adventuretime.exception.PersistenceException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/** Inizializza MySQL usando lo script incluso nelle risorse. */
final class DatabaseInitializer {

    private static final String SCRIPT_RESOURCE =
            "/database/adventuretime.sql";
    private static final String MIGRATION_V2_RESOURCE =
            "/database/migration-v2.sql";
    private static final int CURRENT_SCHEMA_VERSION = 2;

    private DatabaseInitializer() {
    }

    static void initialize(Connection connection) throws PersistenceException {
        int schemaVersion = readSchemaVersion(connection);
        if (schemaVersion == CURRENT_SCHEMA_VERSION) {
            return;
        }

        try {
            if (schemaVersion == 0) {
                upgradeUnversionedSchema(connection);
            }
            String resource = schemaVersion == 0
                    ? SCRIPT_RESOURCE
                    : MIGRATION_V2_RESOURCE;
            String script = readScript(resource);
            executeStatements(connection, script);
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Impossibile inizializzare il database MySQL.", e);
        }
    }

    /**
     * Aggiunge la colonna immagini ai database locali creati con il vecchio
     * schema. Lo script principale si occupa del resto dell'inizializzazione.
     */
    private static void upgradeUnversionedSchema(Connection connection)
            throws SQLException {
        if (tableExists(connection, "hotelrooms")
                && !columnExists(connection, "hotelrooms", "nome_immagine")) {
            execute(connection, "ALTER TABLE hotelrooms "
                    + "ADD COLUMN nome_immagine VARCHAR(255)");
        }

        if (tableExists(connection, "hotel_images")
                && !columnExists(connection, "hotel_images", "nome_immagine")) {
            if (columnExists(connection, "hotel_images", "image_url")) {
                execute(connection, "ALTER TABLE hotel_images "
                        + "CHANGE COLUMN image_url nome_immagine VARCHAR(255) "
                        + "NOT NULL");
            } else {
                execute(connection, "ALTER TABLE hotel_images "
                        + "ADD COLUMN nome_immagine VARCHAR(255) NOT NULL");
            }
        }

        if (tableExists(connection, "bookings")) {
            execute(connection, "ALTER TABLE bookings MODIFY stato ENUM("
                    + "'PENDING_APPROVAL', 'CONFIRMED', 'REJECTED', "
                    + "'CANCELLED') NOT NULL DEFAULT 'PENDING_APPROVAL'");
            addColumnIfMissing(connection, "bookings", "payment_token",
                    "VARCHAR(100) NOT NULL DEFAULT ''");
            addColumnIfMissing(connection, "bookings", "card_holder",
                    "VARCHAR(120) NOT NULL DEFAULT ''");
            addColumnIfMissing(connection, "bookings", "card_last_four",
                    "CHAR(4) NOT NULL DEFAULT ''");
            addColumnIfMissing(connection, "bookings", "payment_completed",
                    "BOOLEAN NOT NULL DEFAULT FALSE");
        }

    }

    private static void addColumnIfMissing(
            Connection connection,
            String table,
            String column,
            String definition
    ) throws SQLException {
        if (!columnExists(connection, table, column)) {
            execute(connection, "ALTER TABLE " + table + " ADD COLUMN "
                    + column + " " + definition);
        }
    }

    private static boolean tableExists(Connection connection, String table)
            throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        try (ResultSet tables = metadata.getTables(
                connection.getCatalog(), null, table, new String[]{"TABLE"})) {
            return tables.next();
        }
    }

    private static boolean columnExists(
            Connection connection,
            String table,
            String column
    ) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        try (ResultSet columns = metadata.getColumns(
                connection.getCatalog(), null, table, column)) {
            return columns.next();
        }
    }

    private static void execute(Connection connection, String sql)
            throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private static int readSchemaVersion(Connection connection) {
        String sql = "SELECT schema_version FROM app_metadata WHERE id = 1";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            return resultSet.next() ? resultSet.getInt("schema_version") : 0;
        } catch (SQLException ignored) {
            return 0;
        }
    }

    private static String readScript(String resource)
            throws PersistenceException {
        try (InputStream stream = DatabaseInitializer.class
                .getResourceAsStream(resource)) {
            if (stream == null) {
                throw new PersistenceException(
                        "Script del database non trovato: " + resource);
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
