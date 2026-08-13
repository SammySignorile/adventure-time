package org.example.adventuretime.ui.gui.util;


import javafx.scene.image.Image;

import java.net.URL;
import java.util.Optional;

/**
 * Carica le immagini degli hotel dal classpath.
 *
 * Le fotografie devono trovarsi in:
 * src/main/resources/images/
 *
 * Nel database viene salvato soltanto il nome del file,
 * per esempio "roma 1.jpg".
 */
public final class HotelImageLoader {

    private static final String IMAGES_FOLDER = "/images/";

    private HotelImageLoader() {
        // Classe di utilità.
    }

    public static Optional<Image> load(String imageFileName) {
        if (imageFileName == null || imageFileName.isBlank()) {
            return Optional.empty();
        }

        URL resource = HotelImageLoader.class.getResource(
                IMAGES_FOLDER + imageFileName.trim()
        );

        if (resource == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(new Image(resource.toExternalForm()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
