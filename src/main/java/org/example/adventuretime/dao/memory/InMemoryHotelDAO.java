package org.example.adventuretime.dao.memory;

import org.example.adventuretime.dao.state.DataStore;
import org.example.adventuretime.dao.state.StateHotelDAO;

public final class InMemoryHotelDAO extends StateHotelDAO {
    public InMemoryHotelDAO(DataStore store) {
        super(store);
    }
}
