package org.example.adventuretime.dao.filesystem;

import org.example.adventuretime.dao.state.DataStore;
import org.example.adventuretime.dao.state.StateHotelDAO;

public final class FileSystemHotelDAO extends StateHotelDAO {
    public FileSystemHotelDAO(DataStore store) {
        super(store);
    }
}
