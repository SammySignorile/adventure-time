package org.example.adventuretime.dao.db;

import org.example.adventuretime.configuration.AppConfig;
import org.example.adventuretime.exception.PersistenceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestisce l'unica connessione JDBC condivisa dai DAO dell'applicazione.
 * Statement e ResultSet vengono chiusi dai singoli DAO; la connessione viene
 * chiusa quando termina l'applicazione.
 */
public final class DBConnectionManager implements AutoCloseable {

    private final String url;
    private final String user;
    private final String password;
    private Connection connection;

    public DBConnectionManager(AppConfig config) {
        this.url = config.database().url();
        this.user = config.database().user();
        this.password = config.database().password();
    }

    public synchronized Connection getConnection()
            throws PersistenceException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = openAndInitializeConnection();
            }
            return connection;
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Impossibile connettersi al database Adventure Time.", e);
        }
    }

    private Connection openAndInitializeConnection()
            throws SQLException, PersistenceException {
        Connection opened = DriverManager.getConnection(url, user, password);
        try {
            DatabaseInitializer.initialize(opened);
            return opened;
        } catch (PersistenceException e) {
            closeAfterInitializationFailure(opened, e);
            throw e;
        }
    }

    private static void closeAfterInitializationFailure(
            Connection opened,
            PersistenceException failure
    ) {
        try {
            opened.close();
        } catch (SQLException closeFailure) {
            failure.addSuppressed(closeFailure);
        }
    }

    @Override
    public synchronized void close() throws PersistenceException {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
            connection = null;
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Impossibile chiudere la connessione al database.", e);
        }
    }
}
