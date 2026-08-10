package org.example.adventuretime.dao.db;

import org.example.adventuretime.dao.UserDAO;
import org.example.adventuretime.exception.PersistenceException;
import org.example.adventuretime.model.Role;
import org.example.adventuretime.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class JdbcUserDAO implements UserDAO {

    private final DBConnectionManager connectionManager;

    public JdbcUserDAO(DBConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Optional<User> findByCredentials(String email, String password)
            throws PersistenceException {
        String sql = """
                SELECT id, nome, cognome, punti, email, password, role
                FROM users
                WHERE email = ? AND password = ?
                """;

        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next()
                        ? Optional.of(mapUser(resultSet))
                        : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante il login sul database.", e);
        }
    }

    @Override
    public Optional<User> findById(long id) throws PersistenceException {
        String sql = """
                SELECT id, nome, cognome, punti, email, password, role
                FROM users
                WHERE id = ?
                """;

        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next()
                        ? Optional.of(mapUser(resultSet))
                        : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante la lettura dell'utente.", e);
        }
    }

    @Override
    public List<User> findAll() throws PersistenceException {
        String sql = """
                SELECT id, nome, cognome, punti, email, password, role
                FROM users
                ORDER BY cognome, nome
                """;
        List<User> users = new ArrayList<>();

        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante la lettura degli utenti.", e);
        }
    }

    @Override
    public User save(User user) throws PersistenceException {
        return user.getId() == 0 ? insert(user) : update(user);
    }

    @Override
    public void updatePoints(long userId, int newPoints)
            throws PersistenceException {
        String sql = "UPDATE users SET punti = ? WHERE id = ?";
        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, newPoints);
            statement.setLong(2, userId);
            int rows = statement.executeUpdate();
            if (rows != 1) {
                throw new PersistenceException(
                        "Utente non trovato durante l'aggiornamento punti.");
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante l'aggiornamento dei punti.", e);
        }
    }

    private User insert(User user) throws PersistenceException {
        String sql = """
                INSERT INTO users(nome, cognome, punti, email, password, role)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {
            bindUser(statement, user);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new PersistenceException(
                            "Il database non ha restituito l'id del nuovo utente.");
                }
                User created = new User(user);
                created.setId(keys.getLong(1));
                return created;
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante il salvataggio dell'utente.", e);
        }
    }

    private User update(User user) throws PersistenceException {
        String sql = """
                UPDATE users
                SET nome = ?, cognome = ?, punti = ?, email = ?,
                    password = ?, role = ?
                WHERE id = ?
                """;

        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            bindUser(statement, user);
            statement.setLong(7, user.getId());
            int rows = statement.executeUpdate();
            if (rows != 1) {
                throw new PersistenceException(
                        "Utente non trovato durante l'aggiornamento.");
            }
            return new User(user);
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante l'aggiornamento dell'utente.", e);
        }
    }

    private static void bindUser(PreparedStatement statement, User user)
            throws SQLException {
        statement.setString(1, user.getFirstName());
        statement.setString(2, user.getLastName());
        statement.setInt(3, user.getPoints());
        statement.setString(4, user.getEmail());
        statement.setString(5, user.getPassword());
        statement.setString(6, user.getRole().name());
    }

    private static User mapUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("id"),
                resultSet.getString("nome"),
                resultSet.getString("cognome"),
                resultSet.getInt("punti"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role"))
        );
    }
}
