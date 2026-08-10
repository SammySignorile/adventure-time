package org.example.adventuretime.ui.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.example.adventuretime.AppContext;
import org.example.adventuretime.bean.BookingRequestBean;
import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.bean.SearchCriteriaBean;
import org.example.adventuretime.exception.AdventureTimeException;
import org.example.adventuretime.model.ExtraService;
import org.example.adventuretime.navigation.SceneId;
import org.example.adventuretime.ui.gui.util.HotelImageLoader;

import java.util.EnumSet;

public final class CheckoutGraphicController {

    @FXML
    private ImageView hotelImageView;
    @FXML
    private Label missingImageLabel;
    @FXML
    private Label hotelLabel;
    @FXML
    private Label stayLabel;
    @FXML
    private Label basePriceLabel;
    @FXML
    private Label extrasPriceLabel;
    @FXML
    private Label discountLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Label pointsAvailableLabel;
    @FXML
    private CheckBox cancellationInsuranceCheck;
    @FXML
    private CheckBox healthInsuranceCheck;
    @FXML
    private CheckBox flexibleDateCheck;
    @FXML
    private CheckBox usePointsCheck;
    @FXML
    private TextField cardNumberField;
    @FXML
    private TextField expiryField;
    @FXML
    private TextField cvvField;
    @FXML
    private TextField cardHolderField;

    private HotelBean hotel;
    private SearchCriteriaBean criteria;

    @FXML
    private void initialize() {
        var flow = AppContext.getInstance().getFlowContext();
        hotel = flow.getSelectedHotel().orElse(null);
        criteria = flow.getLastCriteria().orElse(null);

        if (hotel == null || criteria == null) {
            AlertHelper.error("Il flusso di prenotazione non contiene dati validi.");
            return;
        }

        hotelLabel.setText(hotel.getName() + " - " + hotel.getRoomType());
        showHotelImage(hotel.getImageFileName());
        stayLabel.setText(criteria.getCheckIn() + " → "
                + criteria.getCheckOut() + " | " + criteria.getPeople()
                + " persone");
        pointsAvailableLabel.setText("Punti disponibili: "
                + AppContext.getInstance().getUserSession()
                .requireUser().getPoints());
        refreshQuote();
    }

    private void showHotelImage(String imageFileName) {
        var image = HotelImageLoader.load(imageFileName);
        hotelImageView.setImage(image.orElse(null));
        hotelImageView.setVisible(image.isPresent());
        missingImageLabel.setVisible(image.isEmpty());
        missingImageLabel.setManaged(image.isEmpty());
    }

    @FXML
    private void onOptionsChanged() {
        refreshQuote();
    }

    @FXML
    private void onConfirm() {
        if (!validCardFields()) {
            AlertHelper.error("Compilare tutti i dati della carta.");
            return;
        }

        try {
            var booking = AppContext.getInstance()
                    .manageBookingsController()
                    .confirm(buildRequest());
            AlertHelper.info("Prenotazione confermata",
                    "Codice prenotazione: " + booking.getId()
                            + "\nTotale: €" + booking.getTotalPrice());
            AppContext.getInstance().getSceneRouter().show(SceneId.PROFILE);
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }

    @FXML
    private void onBack() {
        try {
            AppContext.getInstance().getSceneRouter().show(SceneId.HOTEL_LIST);
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }

    private void refreshQuote() {
        if (hotel == null || criteria == null) {
            return;
        }
        try {
            var quote = AppContext.getInstance()
                    .manageBookingsController()
                    .getQuote(buildRequest());
            basePriceLabel.setText("€" + quote.getBasePrice());
            extrasPriceLabel.setText("€" + quote.getExtrasPrice());
            discountLabel.setText("-€" + quote.getPointsDiscount());
            totalLabel.setText("€" + quote.getTotalPrice());
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }

    private BookingRequestBean buildRequest() {
        EnumSet<ExtraService> extras = EnumSet.noneOf(ExtraService.class);
        if (cancellationInsuranceCheck.isSelected()) {
            extras.add(ExtraService.CANCELLATION_INSURANCE);
        }
        if (healthInsuranceCheck.isSelected()) {
            extras.add(ExtraService.HEALTH_INSURANCE);
        }
        if (flexibleDateCheck.isSelected()) {
            extras.add(ExtraService.FLEXIBLE_DATE);
        }

        return new BookingRequestBean(
                hotel.getId(),
                criteria.getCheckIn(),
                criteria.getCheckOut(),
                criteria.getPeople(),
                extras,
                usePointsCheck.isSelected());
    }

    private boolean validCardFields() {
        return notBlank(cardNumberField.getText())
                && notBlank(expiryField.getText())
                && notBlank(cvvField.getText())
                && notBlank(cardHolderField.getText());
    }

    private boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }
}
