package org.example.adventuretime;

import org.example.adventuretime.dao.state.FileSystemDataStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Responsabile test: Sammy Signorile (matricola da inserire). */
class FileSystemDataStoreTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void createsPersistentStateWhenFileDoesNotExist() throws Exception {
        Path database = temporaryDirectory.resolve("state.dat");

        var store = new FileSystemDataStore(database);

        assertTrue(database.toFile().isFile());
        assertEquals(4, store.getState().getUsers().size());
    }

    @Test
    void reloadsPreviouslyPersistedState() throws Exception {
        Path database = temporaryDirectory.resolve("state.dat");
        var firstStore = new FileSystemDataStore(database);
        firstStore.getState().getUsers().clear();
        firstStore.persist();

        var reloadedStore = new FileSystemDataStore(database);

        assertTrue(reloadedStore.getState().getUsers().isEmpty());
    }
}
