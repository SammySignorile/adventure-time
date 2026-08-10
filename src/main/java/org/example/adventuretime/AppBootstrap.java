package org.example.adventuretime;

import org.example.adventuretime.configuration.AppConfig;
import org.example.adventuretime.configuration.ConfigLoader;
import org.example.adventuretime.dao.DAOFactory;
import org.example.adventuretime.dao.db.JdbcDAOFactory;
import org.example.adventuretime.dao.filesystem.FileSystemDAOFactory;
import org.example.adventuretime.dao.memory.InMemoryDAOFactory;
import org.example.adventuretime.exception.AdventureTimeException;

/**
 * Startup object requested by the project specification.
 *
 * It reads the properties before constructing any presentation or persistence
 * component, then selects one coherent DAO family.
 */
public final class AppBootstrap {

    private AppBootstrap() {
    }

    public static AppConfig initialize() throws AdventureTimeException {
        AppConfig config = ConfigLoader.load();

        DAOFactory daoFactory = switch (config.persistenceMode()) {
            case IN_MEMORY -> new InMemoryDAOFactory();
            case FILESYSTEM -> new FileSystemDAOFactory(
                    config.fileSystemPath());
            case DB -> new JdbcDAOFactory(config);
        };

        AppContext.initialize(config, daoFactory);
        return config;
    }
}
