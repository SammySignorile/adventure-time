package org.example.adventuretime.configuration;

import org.example.adventuretime.exception.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

/**
 * Legge il file properties prima di creare interfaccia e DAO.

 */
public final class ConfigLoader {

    private static final String DEFAULT_RESOURCE = "/application.properties";
    private static final String CONFIG_PROPERTY = "adventure.config";
    private static final String USER_HOME_TOKEN = "${user.home}";

    private ConfigLoader() {
        // Classe di utilità: non deve essere istanziata.
    }

    public static AppConfig load() throws ConfigurationException {
        String configResource = normalizeResourceName(
                System.getProperty(CONFIG_PROPERTY, DEFAULT_RESOURCE));

        Properties properties = new Properties();

        try (InputStream stream = ConfigLoader.class
                .getResourceAsStream(configResource)) {

            if (stream == null) {
                throw new ConfigurationException(
                        "File di configurazione non trovato: "
                                + configResource);
            }

            properties.load(stream);

        } catch (IOException e) {
            throw new ConfigurationException(
                    "Impossibile leggere il file di configurazione: "
                            + configResource,
                    e
            );
        }

        AppConfig config = new AppConfig(
                readEnum(properties, "ui.mode", UiMode.class),
                readEnum(properties, "app.mode", AppMode.class),
                readEnum(properties, "persistence.mode", PersistenceMode.class),
                readPath(properties, "filesystem.path"),
                new DatabaseConfig(
                        require(properties, "db.url"),
                        require(properties, "db.user"),
                        require(properties, "db.password")
                ),
                new WindowConfig(
                        readPositiveDouble(properties, "gui.width"),
                        readPositiveDouble(properties, "gui.height")
                )
        );

        validateModes(config);
        return config;
    }

    private static String normalizeResourceName(String resourceName)
            throws ConfigurationException {
        if (resourceName == null || resourceName.isBlank()) {
            throw new ConfigurationException(
                    "La proprietà " + CONFIG_PROPERTY + " non può essere vuota.");
        }
        String trimmed = resourceName.trim();
        return trimmed.startsWith("/") ? trimmed : "/" + trimmed;
    }

    private static void validateModes(AppConfig config)
            throws ConfigurationException {
        boolean demoIsCorrect = config.appMode() == AppMode.DEMO
                && config.persistenceMode() == PersistenceMode.IN_MEMORY;
        boolean fullIsCorrect = config.appMode() == AppMode.FULL
                && config.persistenceMode() != PersistenceMode.IN_MEMORY;

        if (!demoIsCorrect && !fullIsCorrect) {
            throw new ConfigurationException(
                    "Configurazione incoerente: DEMO richiede IN_MEMORY; "
                            + "FULL richiede DB oppure FILESYSTEM.");
        }
    }

    /**
     * Converte una proprietà testuale in Path.
     *
     * Nel properties è possibile scrivere ${user.home}; il programma lo
     * sostituisce con la cartella dell'utente Windows, ad esempio C:/Users/sammy.
     */
    private static Path readPath(Properties properties, String key)
            throws ConfigurationException {
        String value = require(properties, key);
        String userHome = System.getProperty("user.home");
        String expanded = value.replace(USER_HOME_TOKEN, userHome);
        return Path.of(expanded).toAbsolutePath().normalize();
    }

    private static String require(Properties properties, String key)
            throws ConfigurationException {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new ConfigurationException(
                    "Proprietà obbligatoria mancante o vuota: " + key);
        }
        return value.trim();
    }

    private static double readPositiveDouble(
            Properties properties,
            String key
    ) throws ConfigurationException {
        String value = require(properties, key);
        try {
            double number = Double.parseDouble(value);
            if (number <= 0) {
                throw new NumberFormatException("not positive");
            }
            return number;
        } catch (NumberFormatException e) {
            throw new ConfigurationException(
                    "La proprietà " + key
                            + " deve essere un numero positivo.",
                    e
            );
        }
    }

    private static <E extends Enum<E>> E readEnum(
            Properties properties,
            String key,
            Class<E> enumType
    ) throws ConfigurationException {
        String value = require(properties, key)
                .toUpperCase(Locale.ROOT);
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException e) {
            throw new ConfigurationException(
                    "Valore non valido per " + key + ": " + value,
                    e
            );
        }
    }
}
