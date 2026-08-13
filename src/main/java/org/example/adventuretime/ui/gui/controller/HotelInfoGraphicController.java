package org.example.adventuretime.ui.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.adventuretime.bean.HotelBean;

import java.util.List;
import java.util.Optional;

abstract class HotelInfoGraphicController {

    @FXML
    private ImageView hotelImageView;
    @FXML
    private Label missingImageLabel;
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

    protected final void showHotelInfo(HotelBean hotel) {
        nameLabel.setText(hotel.getName());
        cityLabel.setText(hotel.getCity());
        roomTypeLabel.setText(hotel.getRoomType());
        servicesLabel.setText(hotel.getServices());
        distanceLabel.setText(hotel.getDistanceFromCenter());
        capacityLabel.setText("Fino a " + hotel.getCapacity() + " persone");
        priceLabel.setText("€" + hotel.getPricePerNight() + "/notte");
    }

    protected final void setImageNames(List<String> names) {
        imageNames = List.copyOf(names);
        currentImageIndex = 0;
        showCurrentImage();
    }

    protected final Optional<String> getCurrentImageName() {
        if (imageNames.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(imageNames.get(currentImageIndex));
    }

    protected final int getCurrentImagePosition() {
        return imageNames.isEmpty() ? 0 : currentImageIndex + 1;
    }

    protected final int getImageCount() {
        return imageNames.size();
    }

    protected final boolean hasMultipleImages() {
        return imageNames.size() > 1;
    }

    protected final void showImage(
            Optional<Image> image,
            String missingImageText
    ) {
        hotelImageView.setImage(image.orElse(null));
        hotelImageView.setVisible(image.isPresent());
        hotelImageView.setManaged(image.isPresent());

        missingImageLabel.setText(
                image.isPresent() ? "" : missingImageText
        );
        missingImageLabel.setVisible(image.isEmpty());
        missingImageLabel.setManaged(image.isEmpty());
    }

    protected final void showMissingImage(String text) {
        showImage(Optional.empty(), text);
    }

    @FXML
    protected final void onPreviousImage() {
        if (imageNames.isEmpty()) {
            return;
        }

        currentImageIndex = (currentImageIndex - 1 + imageNames.size())
                % imageNames.size();
        showCurrentImage();
    }

    @FXML
    protected final void onNextImage() {
        if (imageNames.isEmpty()) {
            return;
        }

        currentImageIndex = (currentImageIndex + 1) % imageNames.size();
        showCurrentImage();
    }

    protected abstract void showCurrentImage();
}
