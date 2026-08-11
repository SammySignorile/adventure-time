package org.example.adventuretime;

import org.example.adventuretime.configuration.AppMode;
import org.example.adventuretime.configuration.ConfigLoader;
import org.example.adventuretime.configuration.PersistenceMode;
import org.example.adventuretime.configuration.UiMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Responsabile test: Sammy Signorile (matricola da inserire). */
class ConfigLoaderTest {

    @Test
    void loadsModesDirectlyFromApplicationProperties() throws Exception {
        var config = ConfigLoader.load();

        assertEquals(UiMode.GUI, config.uiMode());
        assertEquals(AppMode.FULL, config.appMode());
        assertEquals(PersistenceMode.DB, config.persistenceMode());
        assertTrue(config.database().url()
                .contains("createDatabaseIfNotExist=true"));
    }
}
