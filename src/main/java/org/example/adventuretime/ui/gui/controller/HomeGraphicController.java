package org.example.adventuretime.ui.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.adventuretime.AppContext;
import org.example.adventuretime.exception.AdventureTimeException;
import org.example.adventuretime.navigation.SceneId;

/**
 * Comportamento comune alle home dei due ruoli.
 */
abstract class HomeGraphicController {

    @FXML
    private Label welcomeLabel;

    @FXML
    public final void initialize() {
        try {
            var user = AppContext.getInstance()
                    .loginController().getCurrentUser();
            welcomeLabel.setText("Benvenuto, " + user.getFullName());
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }

    @FXML
    public final void onProfileClick() {
        navigate(SceneId.PROFILE);
    }

    @FXML
    public final void onLogoutClick() {
        AppContext.getInstance().loginController().logout();
        navigate(SceneId.LOGIN);
    }

    protected final void navigate(SceneId scene) {
        try {
            AppContext.getInstance().getSceneRouter().show(scene);
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }
}
