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


public final class HotelCardGraphicController {

    @FXML
    private ImageView hotelImageView;
    @FXML
    private Label missingImageLabel;
    @FXML
    private Button previousImageButton;
    @FXML
    private Button nextImageButton;
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

    private HotelBean hotel;
    private List<String> imageNames = List.of();
    private int currentImageIndex;

    public void setHotel(HotelBean hotel) {
        this.hotel = new HotelBean(hotel);

        nameLabel.setText(hotel.getName());
        cityLabel.setText(hotel.getCity());
        roomTypeLabel.setText(hotel.getRoomType());
        servicesLabel.setText(hotel.getServices());
        distanceLabel.setText(hotel.getDistanceFromCenter());
        capacityLabel.setText("Fino a " + hotel.getCapacity() + " persone");
        priceLabel.setText("€" + hotel.getPricePerNight() + "/notte");

        loadImageNames();
    }

    private void loadImageNames() {
        try {
            imageNames = AppContext.getInstance()
                    .manageBookingsController()
                    .getHotelImageNames(hotel.getId());
        } catch (AdventureTimeException e) {
            imageNames = hotel.getImageFileName() == null
                    || hotel.getImageFileName().isBlank()
                    ? List.of()
                    : List.of(hotel.getImageFileName());
        }

        currentImageIndex = 0;
        showCurrentImage();
    }

    private void showCurrentImage() {
        if (imageNames.isEmpty()) {
            hotelImageView.setImage(null);
            hotelImageView.setVisible(false);
            hotelImageView.setManaged(false);
            missingImageLabel.setVisible(true);
            missingImageLabel.setManaged(true);
            previousImageButton.setVisible(false);
            previousImageButton.setManaged(false);
            nextImageButton.setVisible(false);
            nextImageButton.setManaged(false);
            return;
        }

        String imageName = imageNames.get(currentImageIndex);
        var image = HotelImageLoader.load(imageName);

        hotelImageView.setImage(image.orElse(null));
        hotelImageView.setVisible(image.isPresent());
        hotelImageView.setManaged(image.isPresent());

        missingImageLabel.setText(
                image.isPresent() ? "" : "Foto non trovata"
        );
        missingImageLabel.setVisible(image.isEmpty());
        missingImageLabel.setManaged(image.isEmpty());

        boolean showArrows = imageNames.size() > 1;
        previousImageButton.setVisible(showArrows);
        previousImageButton.setManaged(showArrows);
        nextImageButton.setVisible(showArrows);
        nextImageButton.setManaged(showArrows);
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
    private void onDetailsClick() {
        if (hotel == null) {
            AlertHelper.error("Hotel non inizializzato.");
            return;
        }

        try {
            AppContext context = AppContext.getInstance();
            context.manageBookingsController().selectHotel(hotel.getId());
            context.getSceneRouter().show(SceneId.HOTEL_DETAILS);
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }
}
