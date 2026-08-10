package org.example.adventuretime.dao.db;

import org.example.adventuretime.configuration.AppConfig;
import org.example.adventuretime.exception.PersistenceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Shared JDBC connection provider. It stores only connection parameters and
 * creates a fresh Connection for each DAO operation. Connections are closed by
 * try-with-resources in the DAOs, avoiding a global connection that can expire.
 */
public final class DBConnectionManager {

    private final String url;
    private final String user;
    private final String password;

    public DBConnectionManager(AppConfig config) {
        this.url = config.databaseUrl();
        this.user = config.databaseUser();
        this.password = config.databasePassword();
    }

    public Connection openConnection() throws PersistenceException {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Impossibile connettersi al database Adventure Time.", e);
        }
    }
}
