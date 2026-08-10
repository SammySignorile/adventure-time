package org.example.adventuretime.dao;

import org.example.adventuretime.exception.PersistenceException;

/**
 * Abstract Factory for a coherent family of DAOs.
 * A DB UserDAO is never mixed with a filesystem BookingDAO.
 */
public interface DAOFactory {
    UserDAO getUserDAO();

    HotelDAO getHotelDAO();

    BookingDAO getBookingDAO();

    default void close() throws PersistenceException {
        // Le persistenze senza risorse esterne non devono chiudere nulla.
    }
}
