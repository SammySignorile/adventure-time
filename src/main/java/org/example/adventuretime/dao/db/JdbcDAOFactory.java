package org.example.adventuretime.dao.db;

import org.example.adventuretime.configuration.AppConfig;
import org.example.adventuretime.dao.BookingDAO;
import org.example.adventuretime.dao.DAOFactory;
import org.example.adventuretime.dao.HotelDAO;
import org.example.adventuretime.dao.UserDAO;

/**
 * Concrete Abstract Factory used by FULL + DB mode.
 */
public final class JdbcDAOFactory implements DAOFactory {

    private final UserDAO userDAO;
    private final HotelDAO hotelDAO;
    private final BookingDAO bookingDAO;

    public JdbcDAOFactory(AppConfig config) {
        DBConnectionManager manager = new DBConnectionManager(config);
        userDAO = new JdbcUserDAO(manager);
        hotelDAO = new JdbcHotelDAO(manager);
        bookingDAO = new JdbcBookingDAO(manager);
    }

    @Override
    public UserDAO getUserDAO() {
        return userDAO;
    }

    @Override
    public HotelDAO getHotelDAO() {
        return hotelDAO;
    }

    @Override
    public BookingDAO getBookingDAO() {
        return bookingDAO;
    }
}
