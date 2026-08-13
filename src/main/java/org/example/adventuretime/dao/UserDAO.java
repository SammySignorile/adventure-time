package org.example.adventuretime.dao;

import org.example.adventuretime.exception.PersistenceException;
import org.example.adventuretime.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> findByCredentials(String email, String password)
            throws PersistenceException;

    Optional<User> findById(long id) throws PersistenceException;

    List<User> findAll() throws PersistenceException;

    User save(User user) throws PersistenceException;

    void updatePoints(long userId, int newPoints) throws PersistenceException;
}
