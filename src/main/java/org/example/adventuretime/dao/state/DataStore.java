package org.example.adventuretime.dao.state;

import org.example.adventuretime.exception.PersistenceException;

public interface DataStore {
    DataState getState();

    void persist() throws PersistenceException;
}
