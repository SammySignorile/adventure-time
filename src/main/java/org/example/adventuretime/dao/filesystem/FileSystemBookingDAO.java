package org.example.adventuretime.dao.filesystem;

import org.example.adventuretime.dao.state.DataStore;
import org.example.adventuretime.dao.state.StateBookingDAO;

public final class FileSystemBookingDAO extends StateBookingDAO {
    public FileSystemBookingDAO(DataStore store) {
        super(store);
    }
}
