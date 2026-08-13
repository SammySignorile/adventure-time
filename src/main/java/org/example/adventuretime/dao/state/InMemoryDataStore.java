package org.example.adventuretime.dao.state;

import org.example.adventuretime.exception.PersistenceException;

public final class InMemoryDataStore implements DataStore {

    private final DataState state = DemoData.create();

    @Override
    public DataState getState() {
        return state;
    }

    @Override
    public void persist() throws PersistenceException {
    }
}
