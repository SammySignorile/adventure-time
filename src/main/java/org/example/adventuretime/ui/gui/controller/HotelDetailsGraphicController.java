package org.example.adventuretime.ui.gui.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.adventuretime.AppContext;
import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.exception.AdventureTimeException;
import org.example.adventuretime.navigation.SceneId;
import org.example.adventuretime.ui.gui.util.HotelImageLoader;

public final class HotelDetailsGraphicController
        extends HotelInfoGraphicController {

    @FXML
    private Label imageCounterLabel;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;

    @FXML
    private void initialize() {
        loadHotelDetails();
    }

    private void loadHotelDetails() {
        try {
            var controller = AppContext.getInstance()
                    .manageBookingsController();

            HotelBean hotel = controller.getSelectedHotel();
            showHotelInfo(hotel);
            setImageNames(controller.getSelectedHotelImageNames());

        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }

    @Override
    protected void showCurrentImage() {
        var imageName = getCurrentImageName();
        if (imageName.isEmpty()) {
            showMissingImage("Foto non disponibile");
            imageCounterLabel.setText("0 / 0");
            previousButton.setDisable(true);
            nextButton.setDisable(true);
            return;
        }

        String name = imageName.orElseThrow();
        var image = HotelImageLoader.load(name);
        showImage(image, "Immagine non trovata: " + name);

        imageCounterLabel.setText(
                getCurrentImagePosition() + " / " + getImageCount()
        );

        boolean onlyOneImage = !hasMultipleImages();
        previousButton.setDisable(onlyOneImage);
        nextButton.setDisable(onlyOneImage);
    }

    @FXML
    private void onBookClick() {
        try {
            AppContext.getInstance()
                    .getSceneRouter()
                    .show(SceneId.CHECKOUT);
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }

    @FXML
    private void onBack() {
        try {
            AppContext.getInstance()
                    .getSceneRouter()
                    .show(SceneId.HOTEL_LIST);
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }
}
