package org.example.adventuretime.configuration;

import java.nio.file.Path;

/**
 * Contiene tutti i valori letti dal file properties all'avvio.
 *
 * È un record perché questi dati non devono cambiare mentre l'app è in esecuzione.
 */
public record AppConfig(
        UiMode uiMode,
        AppMode appMode,
        PersistenceMode persistenceMode,
        Path fileSystemPath,
        DatabaseConfig database,
        WindowConfig window
) {
}
