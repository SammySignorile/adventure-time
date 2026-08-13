package org.example.adventuretime.ui.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.adventuretime.AppContext;
import org.example.adventuretime.bean.CredentialsBean;
import org.example.adventuretime.exception.AdventureTimeException;
import org.example.adventuretime.model.Role;
import org.example.adventuretime.navigation.SceneId;

public final class LoginGraphicController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label configurationLabel;

    @FXML
    private void initialize() {
        var config = AppContext.getInstance().getConfig();
        configurationLabel.setText("Modalità: " + config.appMode()
                + " | Persistenza: " + config.persistenceMode());
    }

    @FXML
    private void onLoginClick() {
        try {
            var user = AppContext.getInstance()
                    .loginController()
                    .login(new CredentialsBean(
                            emailField.getText(),
                            passwordField.getText()));

            if (user.getRole() == Role.CLIENTE) {
                AppContext.getInstance().getSceneRouter()
                        .show(SceneId.TRAVELER_HOME);
            } else if (user.getRole() == Role.GESTORE) {
                AppContext.getInstance().getSceneRouter()
                        .show(SceneId.VENDOR_HOME);
            } else {
                AppContext.getInstance().loginController().logout();
                AlertHelper.error("Il ruolo ADMIN non è gestito in questa versione.");
            }
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }
}
