package org.example.adventuretime.dao;

import org.example.adventuretime.exception.PersistenceException;

public interface DAOFactory {
    UserDAO getUserDAO();

    HotelDAO getHotelDAO();

    BookingDAO getBookingDAO();

    default void close() throws PersistenceException {
        // Le persistenze senza risorse esterne non devono chiudere nulla.
    }
}
