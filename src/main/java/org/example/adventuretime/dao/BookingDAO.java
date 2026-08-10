package org.example.adventuretime.dao;

import org.example.adventuretime.exception.PersistenceException;
import org.example.adventuretime.model.Booking;
import org.example.adventuretime.model.BookingStatus;

import java.time.LocalDate;
import java.util.List;

public interface BookingDAO {
    Booking save(Booking booking) throws PersistenceException;

    List<Booking> findByUserId(long userId) throws PersistenceException;

    List<Booking> findByManagerId(long managerId) throws PersistenceException;

    void updateStatus(long bookingId, BookingStatus status)
            throws PersistenceException;

    boolean isHotelAvailable(long hotelId, LocalDate checkIn, LocalDate checkOut)
            throws PersistenceException;
}
