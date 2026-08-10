package org.example.adventuretime.ui.gui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.example.adventuretime.AppContext;
import org.example.adventuretime.bean.BookingBean;
import org.example.adventuretime.bean.HotelBean;
import org.example.adventuretime.exception.AdventureTimeException;
import org.example.adventuretime.navigation.SceneId;
import org.example.adventuretime.ui.gui.util.HotelImageLoader;

import java.math.BigDecimal;

public final class ManageHotelsGraphicController {

    @FXML
    private TableView<HotelBean> hotelsTable;
    @FXML
    private TableColumn<HotelBean, String> hotelNameColumn;
    @FXML
    private TableColumn<HotelBean, String> hotelCityColumn;
    @FXML
    private TableColumn<HotelBean, String> hotelRoomColumn;
    @FXML
    private TableColumn<HotelBean, String> hotelPriceColumn;
    @FXML
    private TableColumn<HotelBean, String> hotelImageColumn;
    @FXML
    private TableView<BookingBean> receivedBookingsTable;
    @FXML
    private TableColumn<BookingBean, String> bookingHotelColumn;
    @FXML
    private TableColumn<BookingBean, String> bookingDatesColumn;
    @FXML
    private TableColumn<BookingBean, String> bookingPeopleColumn;
    @FXML
    private TableColumn<BookingBean, String> bookingTotalColumn;

    @FXML
    private TextField nameField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField roomTypeField;
    @FXML
    private TextField servicesField;
    @FXML
    private TextField distanceField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField capacityField;
    @FXML
    private TextField imageFileField;
    @FXML
    private ImageView previewImageView;
    @FXML
    private Label previewMessageLabel;

    @FXML
    private void initialize() {
        hotelNameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName()));
        hotelCityColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCity()));
        hotelRoomColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRoomType()));
        hotelPriceColumn.setCellValueFactory(data ->
                new SimpleStringProperty("€"
                        + data.getValue().getPricePerNight()));
        hotelImageColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getImageFileName()));

        bookingHotelColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getHotel().getName()));
        bookingDatesColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCheckIn()
                        + " → " + data.getValue().getCheckOut()));
        bookingPeopleColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(
                        data.getValue().getPeople())));
        bookingTotalColumn.setCellValueFactory(data ->
                new SimpleStringProperty("€"
                        + data.getValue().getTotalPrice()));
        refresh();
    }

    @FXML
    private void onPreviewImage() {
        String fileName = imageFileField.getText();
        var image = HotelImageLoader.load(fileName);
        previewImageView.setImage(image.orElse(null));
        previewImageView.setVisible(image.isPresent());

        if (image.isPresent()) {
            previewMessageLabel.setText("Immagine trovata.");
        } else {
            previewMessageLabel.setText(
                    "File non trovato in: ");
        }
    }

    @FXML
    private void onSaveHotel() {
        try {
            HotelBean bean = new HotelBean();
            bean.setName(nameField.getText());
            bean.setCity(cityField.getText());
            bean.setRoomType(roomTypeField.getText());
            bean.setServices(servicesField.getText());
            bean.setDistanceFromCenter(distanceField.getText());
            bean.setPricePerNight(new BigDecimal(
                    priceField.getText().trim().replace(',', '.')));
            bean.setCapacity(Integer.parseInt(capacityField.getText().trim()));

            // Nel DB viene memorizzato soltanto il nome, non il percorso completo.
            bean.setImageFileName(imageFileField.getText().trim());

            AppContext.getInstance()
                    .manageHotelsController()
                    .saveHotel(bean);
            clearForm();
            refresh();
            AlertHelper.info("Struttura salvata",
                    "La struttura è stata aggiunta al catalogo.");
        } catch (NumberFormatException e) {
            AlertHelper.error("Prezzo e capienza devono essere numeri validi.");
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }

    @FXML
    private void onDeleteHotel() {
        HotelBean selected = hotelsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.error("Selezionare una struttura da eliminare.");
            return;
        }
        try {
            AppContext.getInstance()
                    .manageHotelsController()
                    .deleteHotel(selected.getId());
            refresh();
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }

    @FXML
    private void onBack() {
        try {
            AppContext.getInstance()
                    .getSceneRouter()
                    .show(SceneId.VENDOR_HOME);
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }

    private void refresh() {
        try {
            var controller = AppContext.getInstance()
                    .manageHotelsController();
            hotelsTable.getItems().setAll(controller.getMyHotels());
            receivedBookingsTable.getItems().setAll(
                    controller.getReceivedBookings());
        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }

    private void clearForm() {
        nameField.clear();
        cityField.clear();
        roomTypeField.clear();
        servicesField.clear();
        distanceField.clear();
        priceField.clear();
        capacityField.clear();
        imageFileField.clear();
        previewImageView.setImage(null);
        previewImageView.setVisible(false);
        previewMessageLabel.setText(
                "Inserisci il nome del file, poi premi Anteprima.");
    }
}
