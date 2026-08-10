package org.example.adventuretime.ui.gui.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.example.adventuretime.AppContext;
import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.exception.AdventureTimeException;
import org.example.adventuretime.navigation.SceneId;
import org.example.adventuretime.ui.gui.util.HotelImageLoader;

import java.util.List;

public final class HotelDetailsGraphicController {

    @FXML
    private ImageView hotelImageView;
    @FXML
    private Label missingImageLabel;
    @FXML
    private Label imageCounterLabel;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;

    @FXML
    private Label nameLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label roomTypeLabel;
    @FXML
    private Label servicesLabel;
    @FXML
    private Label distanceLabel;
    @FXML
    private Label capacityLabel;
    @FXML
    private Label priceLabel;

    private List<String> imageNames = List.of();
    private int currentImageIndex;

    @FXML
    private void initialize() {
        loadHotelDetails();
    }

    private void loadHotelDetails() {
        try {
            var controller = AppContext.getInstance()
                    .manageBookingsController();

            HotelBean hotel = controller.getSelectedHotel();
            imageNames = controller.getSelectedHotelImageNames();

            nameLabel.setText(hotel.getName());
            cityLabel.setText(hotel.getCity());
            roomTypeLabel.setText(hotel.getRoomType());
            servicesLabel.setText(hotel.getServices());
            distanceLabel.setText(hotel.getDistanceFromCenter());
            capacityLabel.setText(
                    "Fino a " + hotel.getCapacity() + " persone"
            );
            priceLabel.setText(
                    "€" + hotel.getPricePerNight() + "/notte"
            );

            currentImageIndex = 0;
            showCurrentImage();

        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }

    private void showCurrentImage() {
        if (imageNames.isEmpty()) {
            hotelImageView.setImage(null);
            hotelImageView.setVisible(false);
            hotelImageView.setManaged(false);

            missingImageLabel.setVisible(true);
            missingImageLabel.setManaged(true);

            imageCounterLabel.setText("0 / 0");
            previousButton.setDisable(true);
            nextButton.setDisable(true);
            return;
        }

        String imageName = imageNames.get(currentImageIndex);
        var image = HotelImageLoader.load(imageName);

        hotelImageView.setImage(image.orElse(null));
        hotelImageView.setVisible(image.isPresent());
        hotelImageView.setManaged(image.isPresent());

        missingImageLabel.setText(
                image.isPresent()
                        ? ""
                        : "Immagine non trovata: " + imageName
        );
        missingImageLabel.setVisible(image.isEmpty());
        missingImageLabel.setManaged(image.isEmpty());

        imageCounterLabel.setText(
                (currentImageIndex + 1) + " / " + imageNames.size()
        );

        boolean onlyOneImage = imageNames.size() <= 1;
        previousButton.setDisable(onlyOneImage);
        nextButton.setDisable(onlyOneImage);
    }

    @FXML
    private void onPreviousImage() {
        if (imageNames.isEmpty()) {
            return;
        }

        currentImageIndex--;
        if (currentImageIndex < 0) {
            currentImageIndex = imageNames.size() - 1;
        }
        showCurrentImage();
    }

    @FXML
    private void onNextImage() {
        if (imageNames.isEmpty()) {
            return;
        }

        currentImageIndex++;
        if (currentImageIndex >= imageNames.size()) {
            currentImageIndex = 0;
        }
        showCurrentImage();
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
