package org.example.adventuretime.dao.memory;

import org.example.adventuretime.dao.state.DataStore;
import org.example.adventuretime.dao.state.StateBookingDAO;

public final class InMemoryBookingDAO extends StateBookingDAO {
    public InMemoryBookingDAO(DataStore store) {
        super(store);
    }
}
