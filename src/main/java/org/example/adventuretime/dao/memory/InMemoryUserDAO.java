package org.example.adventuretime.dao.memory;

import org.example.adventuretime.dao.state.DataStore;
import org.example.adventuretime.dao.state.StateUserDAO;

public final class InMemoryUserDAO extends StateUserDAO {
    public InMemoryUserDAO(DataStore store) {
        super(store);
    }
}
