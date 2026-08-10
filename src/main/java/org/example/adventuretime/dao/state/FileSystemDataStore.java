package org.example.adventuretime.dao.state;

import org.example.adventuretime.exception.PersistenceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Small student-level filesystem database.
 *
 * The complete state is serialized to one file. A temporary file is written
 * first and then atomically moved when possible, reducing the risk of leaving
 * a half-written database after a crash.
 */
public final class FileSystemDataStore implements DataStore {

    private final Path databasePath;
    private DataState state;

    public FileSystemDataStore(Path databasePath) throws PersistenceException {
        this.databasePath = databasePath;
        this.state = loadOrCreate();
    }

    @Override
    public synchronized DataState getState() {
        return state;
    }

    @Override
    public synchronized void persist() throws PersistenceException {
        try {
            Path parent = databasePath.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Path temporary = databasePath.resolveSibling(
                    databasePath.getFileName() + ".tmp");

            try (ObjectOutputStream output = new ObjectOutputStream(
                    Files.newOutputStream(temporary))) {
                output.writeObject(state);
            }

            try {
                Files.move(
                        temporary,
                        databasePath,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );
            } catch (IOException atomicMoveNotSupported) {
                Files.move(
                        temporary,
                        databasePath,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        } catch (IOException e) {
            throw new PersistenceException(
                    "Impossibile salvare il database su file.", e);
        }
    }

    private DataState loadOrCreate() throws PersistenceException {
        if (!Files.exists(databasePath)) {
            DataState initialState = DemoData.create();
            state = initialState;
            persist();
            return initialState;
        }

        try (ObjectInputStream input = new ObjectInputStream(
                Files.newInputStream(databasePath))) {
            Object object = input.readObject();
            if (!(object instanceof DataState loaded)) {
                throw new PersistenceException(
                        "Il file di persistenza non contiene dati Adventure Time validi.");
            }
            loaded.alignSequences();
            return loaded;
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenceException(
                    "Impossibile caricare il database su file.", e);
        }
    }
}
