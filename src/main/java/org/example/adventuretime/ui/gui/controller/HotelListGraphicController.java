package org.example.adventuretime.ui.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import org.example.adventuretime.AppContext;
import org.example.adventuretime.exception.AdventureTimeException;
import org.example.adventuretime.navigation.SceneId;

import java.io.IOException;
import java.net.URL;

public final class HotelListGraphicController {

    @FXML
    private VBox cardsContainer;

    @FXML
    private void initialize() {
        var hotels = AppContext.getInstance()
                .manageBookingsController()
                .getBufferedResults();

        if (hotels.isEmpty()) {
            javafx.scene.control.Label empty =
                    new javafx.scene.control.Label("Nessun hotel disponibile per i criteri scelti.");
            empty.getStyleClass().add("empty-message");
            cardsContainer.getChildren().add(empty);
            return;
        }

        for (var hotel : hotels) {
            try {
                URL cardResource = getClass().getResource(
                        "/org/example/adventuretime/view/hotel-card.fxml");
                if (cardResource == null) {
                    AlertHelper.error("Vista hotel-card.fxml non trovata.");
                    return;
                }
                FXMLLoader loader = new FXMLLoader(cardResource);
                Parent card = loader.load();
                HotelCardGraphicController controller = loader.getController();
                controller.setHotel(hotel);
                cardsContainer.getChildren().add(card);
            } catch (IOException e) {
                AlertHelper.error("Impossibile creare una card hotel: "
                        + e.getMessage());
                return;
            }
        }
    }

    @FXML
    private void onBack() {
        try {
            AppContext.getInstance().getSceneRouter().show(SceneId.SEARCH_HOTEL);
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }
}
