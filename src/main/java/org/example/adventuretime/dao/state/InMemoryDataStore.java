package org.example.adventuretime.dao.state;

import org.example.adventuretime.exception.PersistenceException;

/**
 * Demo mode store. persist() deliberately does nothing: all changes disappear
 * when the process ends, exactly as required by the exam specification.
 */
public final class InMemoryDataStore implements DataStore {

    private final DataState state = DemoData.create();

    @Override
    public DataState getState() {
        return state;
    }

    @Override
    public void persist() throws PersistenceException {
        // Intentionally empty.
    }
}
