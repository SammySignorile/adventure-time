package org.example.adventuretime.dao.state;

import org.example.adventuretime.exception.PersistenceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Archivio locale basato sulla serializzazione Java.
 *
 * Lo stato completo viene scritto su un file binario tramite
 * ObjectOutputStream e riletto tramite ObjectInputStream. Gli stream sono
 * chiusi automaticamente dai blocchi try-with-resources.
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

            writeTemporaryState(temporary);
            replaceDatabaseFile(temporary);
        } catch (IOException e) {
            throw new PersistenceException(
                    "Impossibile salvare il database su file.", e);
        }
    }

    private void writeTemporaryState(Path temporary) throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(
                Files.newOutputStream(temporary))) {
            output.writeObject(state);
        }
    }

    private void replaceDatabaseFile(Path temporary) throws IOException {
        try {
            Files.move(
                    temporary,
                    databasePath,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE
            );
        } catch (AtomicMoveNotSupportedException atomicMoveNotSupported) {
            Files.move(
                    temporary,
                    databasePath,
                    StandardCopyOption.REPLACE_EXISTING
            );
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
