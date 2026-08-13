package org.example.adventuretime.ui.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.adventuretime.AppContext;
import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.exception.AdventureTimeException;
import org.example.adventuretime.navigation.SceneId;
import org.example.adventuretime.ui.gui.util.HotelImageLoader;

import java.util.List;


public final class HotelCardGraphicController
        extends HotelInfoGraphicController {

    @FXML
    private Button previousImageButton;
    @FXML
    private Button nextImageButton;
    private HotelBean hotel;

    public void setHotel(HotelBean hotel) {
        this.hotel = new HotelBean(hotel);
        showHotelInfo(hotel);
        loadImageNames();
    }

    private void loadImageNames() {
        List<String> names;
        try {
            names = AppContext.getInstance()
                    .manageBookingsController()
                    .getHotelImageNames(hotel.getId());
        } catch (AdventureTimeException e) {
            names = hotel.getImageFileName() == null
                    || hotel.getImageFileName().isBlank()
                    ? List.of()
                    : List.of(hotel.getImageFileName());
        }
        setImageNames(names);
    }

    @Override
    protected void showCurrentImage() {
        var imageName = getCurrentImageName();
        if (imageName.isEmpty()) {
            showMissingImage("Foto non disponibile");
            previousImageButton.setVisible(false);
            previousImageButton.setManaged(false);
            nextImageButton.setVisible(false);
            nextImageButton.setManaged(false);
            return;
        }

        var image = HotelImageLoader.load(imageName.orElseThrow());
        showImage(image, "Foto non trovata");

        boolean showArrows = hasMultipleImages();
        previousImageButton.setVisible(showArrows);
        previousImageButton.setManaged(showArrows);
        nextImageButton.setVisible(showArrows);
        nextImageButton.setManaged(showArrows);
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
