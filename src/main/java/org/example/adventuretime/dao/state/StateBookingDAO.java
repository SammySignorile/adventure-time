package org.example.adventuretime.dao.state;

import org.example.adventuretime.dao.BookingDAO;
import org.example.adventuretime.exception.PersistenceException;
import org.example.adventuretime.model.Booking;
import org.example.adventuretime.model.BookingStatus;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StateBookingDAO implements BookingDAO {

    private final DataStore store;

    public StateBookingDAO(DataStore store) {
        this.store = store;
    }

    @Override
    public synchronized Booking save(Booking booking)
            throws PersistenceException {
        if (!isHotelAvailable(
                booking.getHotelId(),
                booking.getCheckIn(),
                booking.getCheckOut())) {
            throw new PersistenceException(
                    "La struttura non è più disponibile nelle date richieste.");
        }

        Booking created = new Booking(booking);
        created.setId(store.getState().nextBookingId());
        store.getState().getBookings().add(created);
        store.persist();
        return new Booking(created);
    }

    @Override
    public synchronized List<Booking> findByUserId(long userId) {
        return store.getState().getBookings().stream()
                .filter(booking -> booking.getUserId() == userId)
                .sorted(Comparator.comparing(Booking::getCheckIn).reversed())
                .map(Booking::new)
                .toList();
    }

    @Override
    public synchronized List<Booking> findByManagerId(long managerId) {
        Set<Long> hotelIds = store.getState().getHotels().stream()
                .filter(room -> room.getManagerId() == managerId)
                .map(room -> room.getId())
                .collect(Collectors.toSet());

        return store.getState().getBookings().stream()
                .filter(booking -> hotelIds.contains(booking.getHotelId()))
                .sorted(Comparator.comparing(Booking::getCheckIn).reversed())
                .map(Booking::new)
                .toList();
    }

    @Override
    public synchronized boolean isHotelAvailable(
            long hotelId,
            LocalDate checkIn,
            LocalDate checkOut
    ) {
        return store.getState().getBookings().stream()
                .filter(booking -> booking.getHotelId() == hotelId)
                .filter(booking -> booking.getStatus()
                        == BookingStatus.CONFIRMED)
                .noneMatch(booking -> overlaps(
                        booking.getCheckIn(),
                        booking.getCheckOut(),
                        checkIn,
                        checkOut
                ));
    }

    private static boolean overlaps(
            LocalDate existingCheckIn,
            LocalDate existingCheckOut,
            LocalDate requestedCheckIn,
            LocalDate requestedCheckOut
    ) {
        return existingCheckIn.isBefore(requestedCheckOut)
                && existingCheckOut.isAfter(requestedCheckIn);
    }
}
