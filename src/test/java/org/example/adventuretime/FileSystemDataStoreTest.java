package org.example.adventuretime;

import org.example.adventuretime.dao.state.FileSystemDataStore;
import org.example.adventuretime.exception.PersistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Responsabile test: Sammy Signorile (matricola da inserire). */
class FileSystemDataStoreTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void createsAndReloadsPersistentState() throws Exception {
        Path database = temporaryDirectory.resolve("state.dat");

        var store = new FileSystemDataStore(database);

        assertTrue(database.toFile().isFile());
        assertEquals(4, store.getState().getUsers().size());
        store.getState().getUsers().clear();
        store.persist();

        var reloadedStore = new FileSystemDataStore(database);

        assertTrue(reloadedStore.getState().getUsers().isEmpty());
    }

    @Test
    void wrapsInvalidFileErrorKeepingTheOriginalCause() throws Exception {
        Path database = temporaryDirectory.resolve("invalid-state.dat");
        Files.writeString(database, "contenuto non serializzato");

        PersistenceException exception = assertThrows(
                PersistenceException.class,
                () -> new FileSystemDataStore(database)
        );

        assertEquals(
                "Impossibile caricare il database su file.",
                exception.getMessage()
        );
        assertInstanceOf(IOException.class, exception.getCause());
    }
}
