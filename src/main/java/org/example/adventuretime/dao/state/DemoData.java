package org.example.adventuretime.dao.state;

import org.example.adventuretime.model.Booking;
import org.example.adventuretime.model.BookingStatus;
import org.example.adventuretime.model.HotelRoom;
import org.example.adventuretime.model.Role;
import org.example.adventuretime.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public final class DemoData {

    private DemoData() {
    }

    public static DataState create() {
        DataState state = new DataState();

        state.getUsers().add(new User(
                1, "Mario", "Rossi", 1000,
                "mario@test.com", "1234", Role.CLIENTE));
        state.getUsers().add(new User(
                2, "Luca", "Bianchi", 100,
                "luca@test.com", "1234", Role.CLIENTE));
        state.getUsers().add(new User(
                3, "Michele", "Damiano", 432,
                "mike@gmail.com", "1234", Role.GESTORE));
        state.getUsers().add(new User(
                4, "Matteo", "Leoncino", 34431,
                "leoncino@gmail.com", "1234", Role.GESTORE));

        state.getHotels().add(new HotelRoom(
                1, 3, "Hotel Roma Center", "Roma", "Camera Singola",
                "WiFi • Colazione inclusa • Piscina", "500m dal centro",
                new BigDecimal("120.00"), "roma 1.jpg", 1));
        state.getHotels().add(new HotelRoom(
                2, 3, "Hotel Roma Termini", "Roma", "Camera Doppia",
                "WiFi • Colazione • Aria condizionata", "300m dal centro",
                new BigDecimal("110.00"), "roma 2.jpg", 2));
        state.getHotels().add(new HotelRoom(
                3, 3, "Hotel Colosseo View", "Roma", "Suite Deluxe",
                "Vista Colosseo • WiFi • Spa", "150m dal centro",
                new BigDecimal("280.00"), "roma 3.jpg", 4));
        state.getHotels().add(new HotelRoom(
                4, 4, "Hotel Trastevere Cozy", "Roma", "Camera Matrimoniale",
                "WiFi • Colazione • Terrazza", "800m dal centro",
                new BigDecimal("130.00"), "roma 4.jpg", 2));
        state.getHotels().add(new HotelRoom(
                5, 4, "Hotel Milano Luxury", "Milano", "Suite Deluxe",
                "Spa • Piscina • Ristorante", "200m dal centro",
                new BigDecimal("250.00"), "milano 1.jpg", 4));
        state.getHotels().add(new HotelRoom(
                6, 4, "Hotel Napoli Mare", "Napoli", "Camera Matrimoniale",
                "Vista mare • WiFi • Colazione", "1km dal centro",
                new BigDecimal("90.00"), "napoli 1.jpg", 2));

        state.getBookings().add(new Booking(
                1, 1, 1,
                LocalDate.of(2026, 9, 10),
                LocalDate.of(2026, 9, 15),
                1,
                new BigDecimal("600.00"),
                Set.of(),
                0,
                BookingStatus.CONFIRMED));

        state.alignSequences();
        return state;
    }
}
