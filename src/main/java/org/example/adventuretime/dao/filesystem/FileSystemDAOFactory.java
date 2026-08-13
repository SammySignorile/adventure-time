package org.example.adventuretime.dao.filesystem;

import org.example.adventuretime.dao.BookingDAO;
import org.example.adventuretime.dao.DAOFactory;
import org.example.adventuretime.dao.HotelDAO;
import org.example.adventuretime.dao.UserDAO;
import org.example.adventuretime.dao.state.DataStore;
import org.example.adventuretime.dao.state.FileSystemDataStore;
import org.example.adventuretime.exception.PersistenceException;

import java.nio.file.Path;

public final class FileSystemDAOFactory implements DAOFactory {

    private final UserDAO userDAO;
    private final HotelDAO hotelDAO;
    private final BookingDAO bookingDAO;

    public FileSystemDAOFactory(Path databasePath) throws PersistenceException {
        DataStore store = new FileSystemDataStore(databasePath);
        userDAO = new FileSystemUserDAO(store);
        hotelDAO = new FileSystemHotelDAO(store);
        bookingDAO = new FileSystemBookingDAO(store);
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
