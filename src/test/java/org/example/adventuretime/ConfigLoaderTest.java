package org.example.adventuretime;

import org.example.adventuretime.configuration.AppMode;
import org.example.adventuretime.configuration.ConfigLoader;
import org.example.adventuretime.configuration.PersistenceMode;
import org.example.adventuretime.configuration.UiMode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Responsabile test: Sammy Signorile (matricola da inserire). */
class ConfigLoaderTest {

    private static final String CONFIG_PROPERTY = "adventure.config";

    @AfterEach
    void restoreDefaultConfiguration() {
        System.clearProperty(CONFIG_PROPERTY);
    }

    @Test
    void defaultProfileStartsGuiInMemoryDemo() throws Exception {
        var config = ConfigLoader.load();

        assertEquals(UiMode.GUI, config.uiMode());
        assertEquals(AppMode.DEMO, config.appMode());
        assertEquals(PersistenceMode.IN_MEMORY, config.persistenceMode());
    }

    @Test
    void cliProfileKeepsDemoDataInMemory() throws Exception {
        System.setProperty(CONFIG_PROPERTY, "application-cli.properties");

        var config = ConfigLoader.load();

        assertEquals(UiMode.CLI, config.uiMode());
        assertEquals(AppMode.DEMO, config.appMode());
        assertEquals(PersistenceMode.IN_MEMORY, config.persistenceMode());
    }

    @Test
    void fullFileSystemProfileEnablesPersistence() throws Exception {
        System.setProperty(
                CONFIG_PROPERTY,
                "/application-full-filesystem.properties");

        var config = ConfigLoader.load();

        assertEquals(UiMode.GUI, config.uiMode());
        assertEquals(AppMode.FULL, config.appMode());
        assertEquals(PersistenceMode.FILESYSTEM, config.persistenceMode());
    }
}
