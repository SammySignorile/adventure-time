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

        state.getHotels().add(withDetails(
                hotel(1, 3, "Hotel Roma Center", "Camera Singola",
                        "120.00", "roma 1.jpg", 1),
                "WiFi • Colazione inclusa • Piscina", "500m dal centro"));
        state.getHotels().add(withDetails(
                hotel(2, 3, "Hotel Roma Termini", "Camera Doppia",
                        "110.00", "roma 2.jpg", 2),
                "WiFi • Colazione • Aria condizionata", "300m dal centro"));
        state.getHotels().add(withDetails(
                hotel(3, 3, "Hotel Colosseo View", "Suite Deluxe",
                        "280.00", "roma 3.jpg", 4),
                "Vista Colosseo • WiFi • Spa", "150m dal centro"));
        state.getHotels().add(withDetails(
                hotel(4, 4, "Hotel Trastevere Cozy", "Camera Matrimoniale",
                        "130.00", "roma centro.jpg", 2),
                "WiFi • Colazione • Terrazza", "800m dal centro"));

        HotelRoom milan = withDetails(
                hotel(5, 4, "Hotel Milano Luxury", "Suite Deluxe",
                        "250.00", "stanza 2.jpg", 4),
                "Spa • Piscina • Ristorante", "200m dal centro");
        milan.setCity("Milano");
        state.getHotels().add(milan);

        HotelRoom naples = withDetails(
                hotel(6, 4, "Hotel Napoli Mare", "Camera Matrimoniale",
                        "90.00", "colazione 2.jpg", 2),
                "Vista mare • WiFi • Colazione", "1km dal centro");
        naples.setCity("Napoli");
        state.getHotels().add(naples);

        Booking booking = new Booking();
        booking.setId(1);
        booking.setUserId(1);
        booking.setHotelId(1);
        booking.setCheckIn(LocalDate.of(2026, 9, 10));
        booking.setCheckOut(LocalDate.of(2026, 9, 15));
        booking.setPeople(1);
        booking.setTotalPrice(new BigDecimal("600.00"));
        booking.setExtras(Set.of());
        booking.setStatus(BookingStatus.CONFIRMED);
        state.getBookings().add(booking);

        state.alignSequences();
        return state;
    }

    private static HotelRoom hotel(
            long id,
            long managerId,
            String name,
            String roomType,
            String price,
            String image,
            int capacity
    ) {
        HotelRoom room = new HotelRoom();
        room.setId(id);
        room.setManagerId(managerId);
        room.setName(name);
        room.setCity("Roma");
        room.setRoomType(roomType);
        room.setPricePerNight(new BigDecimal(price));
        room.setImageFileName(image);
        room.setCapacity(capacity);
        return room;
    }

    private static HotelRoom withDetails(
            HotelRoom room,
            String services,
            String distance
    ) {
        room.setServices(services);
        room.setDistanceFromCenter(distance);
        return room;
    }
}
