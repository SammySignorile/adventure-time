package org.example.adventuretime.dao.state;

import org.example.adventuretime.dao.UserDAO;
import org.example.adventuretime.exception.PersistenceException;
import org.example.adventuretime.model.User;

import java.util.List;
import java.util.Optional;

public class StateUserDAO implements UserDAO {

    private final DataStore store;

    public StateUserDAO(DataStore store) {
        this.store = store;
    }

    @Override
    public synchronized Optional<User> findByCredentials(
            String email,
            String password
    ) {
        return store.getState().getUsers().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .filter(user -> user.getPassword().equals(password))
                .findFirst()
                .map(User::new);
    }

    @Override
    public synchronized Optional<User> findById(long id) {
        return store.getState().getUsers().stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .map(User::new);
    }

    @Override
    public synchronized List<User> findAll() {
        return store.getState().getUsers().stream()
                .map(User::new)
                .toList();
    }

    @Override
    public synchronized User save(User user) throws PersistenceException {
        DataState state = store.getState();
        if (user.getId() == 0) {
            User created = new User(user);
            created.setId(state.nextUserId());
            state.getUsers().add(created);
            store.persist();
            return new User(created);
        }

        User existing = state.getUsers().stream()
                .filter(candidate -> candidate.getId() == user.getId())
                .findFirst()
                .orElseThrow(() -> new PersistenceException(
                        "Utente non trovato: " + user.getId()));

        copyValues(user, existing);
        store.persist();
        return new User(existing);
    }

    @Override
    public synchronized void updatePoints(long userId, int newPoints)
            throws PersistenceException {
        User user = store.getState().getUsers().stream()
                .filter(candidate -> candidate.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new PersistenceException(
                        "Utente non trovato: " + userId));

        user.setPoints(newPoints);
        store.persist();
    }

    private static void copyValues(User source, User destination) {
        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setPoints(source.getPoints());
        destination.setEmail(source.getEmail());
        destination.setPassword(source.getPassword());
        destination.setRole(source.getRole());
    }
}
