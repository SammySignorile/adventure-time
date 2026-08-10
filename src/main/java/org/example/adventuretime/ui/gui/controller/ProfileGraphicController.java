package org.example.adventuretime.ui.gui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.example.adventuretime.AppContext;
import org.example.adventuretime.bean.BookingBean;
import org.example.adventuretime.bean.UserBean;
import org.example.adventuretime.exception.AdventureTimeException;
import org.example.adventuretime.navigation.SceneId;


import java.util.List;

public final class ProfileGraphicController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Label pointsLabel;

    @FXML
    private TableView<BookingBean> bookingsTable;

    @FXML
    private TableColumn<BookingBean, String> hotelColumn;

    @FXML
    private TableColumn<BookingBean, String> datesColumn;

    @FXML
    private TableColumn<BookingBean, String> peopleColumn;

    @FXML
    private TableColumn<BookingBean, String> totalColumn;

    @FXML
    private TableColumn<BookingBean, String> extrasColumn;

    @FXML
    private void initialize() {
        configureTable();
        loadData();
    }

    /**
     * Configura il modo in cui ogni attributo di BookingBean
     * viene visualizzato nelle colonne della tabella.
     */
    private void configureTable() {
        hotelColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getHotel().getName()
                )
        );

        datesColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getCheckIn()
                                + " → "
                                + data.getValue().getCheckOut()
                )
        );

        peopleColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        String.valueOf(data.getValue().getPeople())
                )
        );

        totalColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        "€" + data.getValue().getTotalPrice()
                )
        );

        extrasColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getExtrasLabel()
                )
        );
    }

    /**
     * Carica i dati dell'utente e le prenotazioni corrette
     * in base al suo ruolo.
     */
    private void loadData() {
        try {
            AppContext context = AppContext.getInstance();

            /*
             * Il LoginApplicationController gestisce anche
             * l'utente autenticato e la sessione.
             */
            UserBean user = context
                    .loginController()
                    .getCurrentUser();

            List<BookingBean> bookings;

            if (context.getUserSession().isVendor()) {
                /*
                 * L'albergatore visualizza le prenotazioni
                 * ricevute per le proprie strutture.
                 */
                bookings = context
                        .manageHotelsController()
                        .getReceivedBookings();
            } else {
                /*
                 * Il viaggiatore visualizza le prenotazioni
                 * che ha effettuato.
                 */
                bookings = context
                        .manageBookingsController()
                        .getMyBookings();
            }

            nameLabel.setText(user.getFullName());
            emailLabel.setText(user.getEmail());
            roleLabel.setText(user.getRoleLabel());
            pointsLabel.setText(String.valueOf(user.getPoints()));

            bookingsTable.getItems().setAll(bookings);

        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }

    @FXML
    private void onBack() {
        try {
            AppContext context = AppContext.getInstance();

            SceneId target;

            if (context.getUserSession().isVendor()) {
                target = SceneId.VENDOR_HOME;
            } else {
                target = SceneId.TRAVELER_HOME;
            }

            context.getSceneRouter().show(target);

        } catch (AdventureTimeException e) {
            AlertHelper.error(e.getMessage());
        }
    }
}

