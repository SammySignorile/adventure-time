package org.example.adventuretime.configuration;

import java.nio.file.Path;

/**
 * Contiene tutti i valori letti dal file properties all'avvio.
 *
 * È un record perché questi dati non devono cambiare mentre l'app è in esecuzione.
 */
@SuppressWarnings("java:S107")
public record AppConfig(
        UiMode uiMode,
        AppMode appMode,
        PersistenceMode persistenceMode,
        Path fileSystemPath,
        Path hotelImagesPath,
        String databaseUrl,
        String databaseUser,
        String databasePassword,
        double guiWidth,
        double guiHeight
) {
}
