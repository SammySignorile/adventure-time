package org.example.adventuretime.dao.state;

import org.example.adventuretime.model.Booking;
import org.example.adventuretime.model.HotelRoom;
import org.example.adventuretime.model.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataState implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<User> users = new ArrayList<>();
    private final List<HotelRoom> hotels = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();
    private long nextUserId = 1;
    private long nextHotelId = 1;
    private long nextBookingId = 1;

    public List<User> getUsers() {
        return users;
    }

    public List<HotelRoom> getHotels() {
        return hotels;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public long nextUserId() {
        return nextUserId++;
    }

    public long nextHotelId() {
        return nextHotelId++;
    }

    public long nextBookingId() {
        return nextBookingId++;
    }

    public void alignSequences() {
        nextUserId = users.stream().mapToLong(User::getId).max().orElse(0) + 1;
        nextHotelId = hotels.stream().mapToLong(HotelRoom::getId).max().orElse(0) + 1;
        nextBookingId = bookings.stream().mapToLong(Booking::getId).max().orElse(0) + 1;
    }
}
