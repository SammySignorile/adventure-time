package org.example.adventuretime.ui.gui;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.adventuretime.AppContext;
import org.example.adventuretime.exception.ConfigurationException;
import org.example.adventuretime.navigation.SceneId;
import org.example.adventuretime.navigation.SceneRouter;

public final class GuiApplication extends Application {

    @Override
    public void start(Stage stage) {
        AppContext context = AppContext.getInstance();
        SceneRouter router = new SceneRouter(stage, context.getConfig());
        context.setSceneRouter(router);

        stage.setTitle("Adventure Time");
        stage.setMinWidth(850);
        stage.setMinHeight(620);

        try {
            router.show(SceneId.LOGIN);
            stage.show();
        } catch (ConfigurationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore di avvio");
            alert.setHeaderText("Impossibile caricare Adventure Time");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
