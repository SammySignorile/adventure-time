package org.example.adventuretime.ui.gui.controller;

import javafx.fxml.FXML;
import org.example.adventuretime.navigation.SceneId;

public final class TravelerHomeGraphicController
        extends HomeGraphicController {

    @FXML
    private void onSearchHotelClick() {
        navigate(SceneId.SEARCH_HOTEL);
    }

}
