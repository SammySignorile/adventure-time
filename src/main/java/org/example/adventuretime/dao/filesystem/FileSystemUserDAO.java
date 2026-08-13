package org.example.adventuretime.dao.filesystem;

import org.example.adventuretime.dao.state.DataStore;
import org.example.adventuretime.dao.state.StateUserDAO;

public final class FileSystemUserDAO extends StateUserDAO {
    public FileSystemUserDAO(DataStore store) {
        super(store);
    }
}
