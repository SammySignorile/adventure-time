package org.example.adventuretime.dao;

/**
 * Abstract Factory for a coherent family of DAOs.
 * A DB UserDAO is never mixed with a filesystem BookingDAO.
 */
public interface DAOFactory {
    UserDAO getUserDAO();

    HotelDAO getHotelDAO();

    BookingDAO getBookingDAO();
}
