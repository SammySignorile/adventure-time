package org.example.adventuretime.ui.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.adventuretime.AppContext;
import org.example.adventuretime.bean.SearchCriteriaBean;
import org.example.adventuretime.exception.AdventureTimeException;
import org.example.adventuretime.navigation.SceneId;

import java.math.BigDecimal;

public final class SearchHotelGraphicController {

    @FXML
    private TextField cityField;
    @FXML
    private DatePicker checkInField;
    @FXML
    private DatePicker checkOutField;
    @FXML
    private TextField peopleField;
    @FXML
    private TextField priceField;

    @FXML
    private void onSearchClick() {
        try {
            int people = Integer.parseInt(peopleField.getText().trim());
            BigDecimal price = new BigDecimal(
                    priceField.getText().trim().replace(',', '.'));

            SearchCriteriaBean criteria = new SearchCriteriaBean(
                    cityField.getText(),
                    checkInField.getValue(),
                    checkOutField.getValue(),
                    people,
                    price
            );

            AppContext.getInstance().manageBookingsController().search(criteria);
            AppContext.getInstance().getSceneRouter().show(SceneId.HOTEL_LIST);
        } catch (NumberFormatException e) {
            AlertHelper.error("Persone e prezzo devono essere numeri validi.");
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }

    @FXML
    private void onBack() {
        try {
            AppContext.getInstance().getSceneRouter().show(SceneId.TRAVELER_HOME);
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }
}
