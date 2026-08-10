package org.example.adventuretime;

import org.example.adventuretime.configuration.AppConfig;
import org.example.adventuretime.exception.AdventureTimeException;
import org.example.adventuretime.ui.ApplicationInterface;
import org.example.adventuretime.ui.InterfaceFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Plain Java entry point. JavaFX is launched only after configuration and DAO
 * selection have completed successfully.
 */
public final class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private Main() {
    }

    public static void main(String[] args) {
        try {
            AppConfig config = AppBootstrap.initialize();
            ApplicationInterface applicationInterface =
                    InterfaceFactory.create(config.uiMode());
            applicationInterface.start(args);
        } catch (AdventureTimeException | RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Avvio non riuscito", e);
        }
    }
}
