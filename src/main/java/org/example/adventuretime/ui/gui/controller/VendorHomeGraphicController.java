package org.example.adventuretime.ui.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.adventuretime.AppContext;
import org.example.adventuretime.exception.ConfigurationException;
import org.example.adventuretime.navigation.SceneId;

public final class VendorHomeGraphicController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private void initialize() {
        var user = AppContext.getInstance().getUserSession().requireUser();
        welcomeLabel.setText("Benvenuto, " + user.getFullName());
    }

    @FXML
    private void onManageHotelsClick() {
        navigate(SceneId.MANAGE_HOTELS);
    }

    @FXML
    private void onProfileClick() {
        navigate(SceneId.PROFILE);
    }

    @FXML
    private void onLogoutClick() {
        AppContext.getInstance().loginController().logout();
        navigate(SceneId.LOGIN);
    }

    private void navigate(SceneId scene) {
        try {
            AppContext.getInstance().getSceneRouter().show(scene);
        } catch (ConfigurationException e) {
            AlertHelper.error(e.getMessage());
        }
    }
}
