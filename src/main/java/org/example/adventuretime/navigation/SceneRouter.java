package org.example.adventuretime.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.adventuretime.configuration.AppConfig;
import org.example.adventuretime.exception.ConfigurationException;

import java.io.IOException;
import java.net.URL;

public final class SceneRouter {

    private final Stage primaryStage;
    private final AppConfig config;

    public SceneRouter(Stage primaryStage, AppConfig config) {
        this.primaryStage = primaryStage;
        this.config = config;
    }

    public void show(SceneId sceneId) throws ConfigurationException {
        URL resource = SceneRouter.class.getResource(
                sceneId.getResourcePath());
        if (resource == null) {
            throw new ConfigurationException(
                    "Vista FXML non trovata: " + sceneId.getResourcePath());
        }

        try {
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            Scene scene = primaryStage.getScene();
            if (scene == null) {
                scene = new Scene(
                        root,
                        config.window().width(),
                        config.window().height()
                );
                primaryStage.setScene(scene);
            } else {
                scene.setRoot(root);
            }

            URL css = SceneRouter.class.getResource(
                    "/org/example/adventuretime/css/adventure-time.css");
            if (css != null && !scene.getStylesheets().contains(
                    css.toExternalForm())) {
                scene.getStylesheets().add(css.toExternalForm());
            }
        } catch (IOException e) {
            throw new ConfigurationException(
                    "Impossibile caricare la vista " + sceneId, e);
        }
    }
}
