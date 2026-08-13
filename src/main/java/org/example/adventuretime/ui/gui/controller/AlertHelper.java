package org.example.adventuretime.ui.gui.controller;

import javafx.scene.control.Alert;

final class AlertHelper {

    private AlertHelper() {
    }

    static void info(String title, String message) {
        show(Alert.AlertType.INFORMATION, title, message);
    }

    static void error(String message) {
        show(Alert.AlertType.ERROR, "Errore", message);
    }

    private static void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
