package org.example.adventuretime.dao.memory;

import org.example.adventuretime.dao.BookingDAO;
import org.example.adventuretime.dao.DAOFactory;
import org.example.adventuretime.dao.HotelDAO;
import org.example.adventuretime.dao.UserDAO;
import org.example.adventuretime.dao.state.DataStore;
import org.example.adventuretime.dao.state.InMemoryDataStore;

public final class InMemoryDAOFactory implements DAOFactory {

    private final UserDAO userDAO;
    private final HotelDAO hotelDAO;
    private final BookingDAO bookingDAO;

    public InMemoryDAOFactory() {
        DataStore store = new InMemoryDataStore();
        userDAO = new InMemoryUserDAO(store);
        hotelDAO = new InMemoryHotelDAO(store);
        bookingDAO = new InMemoryBookingDAO(store);
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
