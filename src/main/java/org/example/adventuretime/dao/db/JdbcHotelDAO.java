package org.example.adventuretime.dao.db;

import org.example.adventuretime.dao.HotelDAO;
import org.example.adventuretime.exception.PersistenceException;
import org.example.adventuretime.model.HotelRoom;
import org.example.adventuretime.model.HotelSearchCriteria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class JdbcHotelDAO implements HotelDAO {

    private final DBConnectionManager connectionManager;

    public JdbcHotelDAO(DBConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Optional<HotelRoom> findById(long id) throws PersistenceException {
        String sql = """
                SELECT id, gestore_id, nome, citta, tipo_camera, servizi,
                       distanza_centro, prezzo_notte, nome_immagine, capienza
                FROM hotelrooms
                WHERE id = ?
                """;

        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next()
                        ? Optional.of(mapHotel(resultSet))
                        : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante la lettura della struttura.", e);
        }
    }

    @Override
    public List<HotelRoom> findByCriteria(HotelSearchCriteria criteria)
            throws PersistenceException {
        String sql = """
                SELECT id, gestore_id, nome, citta, tipo_camera, servizi,
                       distanza_centro, prezzo_notte, nome_immagine, capienza
                FROM hotelrooms
                WHERE LOWER(citta) LIKE LOWER(?)
                  AND prezzo_notte <= ?
                  AND capienza >= ?
                ORDER BY prezzo_notte, nome
                """;

        List<HotelRoom> result = new ArrayList<>();
        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + criteria.city().trim() + "%");
            statement.setBigDecimal(2, criteria.maximumPricePerNight());
            statement.setInt(3, criteria.people());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(mapHotel(resultSet));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante la ricerca delle strutture.", e);
        }
    }

    @Override
    public List<HotelRoom> findByManagerId(long managerId)
            throws PersistenceException {
        String sql = """
                SELECT id, gestore_id, nome, citta, tipo_camera, servizi,
                       distanza_centro, prezzo_notte, nome_immagine, capienza
                FROM hotelrooms
                WHERE gestore_id = ?
                ORDER BY citta, nome
                """;

        List<HotelRoom> result = new ArrayList<>();
        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, managerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(mapHotel(resultSet));
                }
            }
            return result;
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante la lettura delle strutture del venditore.", e);
        }
    }

    @Override
    public List<String> findImageNamesByHotelId(long hotelId)
            throws PersistenceException {

        String sql = """
                SELECT nome_immagine
                FROM hotel_images
                WHERE hotel_id = ?
                ORDER BY id
                """;

        List<String> imageNames = new ArrayList<>();

        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, hotelId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("nome_immagine");
                    if (name != null && !name.isBlank()) {
                        imageNames.add(name);
                    }
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante la lettura delle immagini dell'hotel.", e);
        }

        if (imageNames.isEmpty()) {
            return HotelDAO.super.findImageNamesByHotelId(hotelId);
        }

        return List.copyOf(imageNames);
    }

    @Override
    public HotelRoom save(HotelRoom hotel) throws PersistenceException {
        return hotel.getId() == 0 ? insert(hotel) : update(hotel);
    }

    @Override
    public void delete(long id, long managerId) throws PersistenceException {
        String sql = "DELETE FROM hotelrooms WHERE id = ? AND gestore_id = ?";
        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.setLong(2, managerId);
            int rows = statement.executeUpdate();
            if (rows != 1) {
                throw new PersistenceException(
                        "Struttura non trovata o non appartenente al venditore.");
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Impossibile eliminare la struttura. "
                            + "Controllare che non abbia prenotazioni associate.", e);
        }
    }

    private HotelRoom insert(HotelRoom hotel) throws PersistenceException {
        String sql = """
                INSERT INTO hotelrooms(
                    gestore_id, nome, citta, tipo_camera, servizi,
                    distanza_centro, prezzo_notte, nome_immagine, capienza
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {
            bindHotel(statement, hotel);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new PersistenceException(
                            "Il database non ha restituito l'id della struttura.");
                }
                HotelRoom created = new HotelRoom(hotel);
                created.setId(keys.getLong(1));
                return created;
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante la creazione della struttura.", e);
        }
    }

    private HotelRoom update(HotelRoom hotel) throws PersistenceException {
        String sql = """
                UPDATE hotelrooms
                SET gestore_id = ?, nome = ?, citta = ?, tipo_camera = ?,
                    servizi = ?, distanza_centro = ?, prezzo_notte = ?,
                    nome_immagine = ?, capienza = ?
                WHERE id = ? AND gestore_id = ?
                """;

        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            bindHotel(statement, hotel);
            statement.setLong(10, hotel.getId());
            statement.setLong(11, hotel.getManagerId());
            int rows = statement.executeUpdate();
            if (rows != 1) {
                throw new PersistenceException(
                        "Struttura non trovata o non appartenente al venditore.");
            }
            return new HotelRoom(hotel);
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante l'aggiornamento della struttura.", e);
        }
    }

    private static void bindHotel(
            PreparedStatement statement,
            HotelRoom hotel
    ) throws SQLException {
        statement.setLong(1, hotel.getManagerId());
        statement.setString(2, hotel.getName());
        statement.setString(3, hotel.getCity());
        statement.setString(4, hotel.getRoomType());
        statement.setString(5, hotel.getServices());
        statement.setString(6, hotel.getDistanceFromCenter());
        statement.setBigDecimal(7, hotel.getPricePerNight());
        statement.setString(8, hotel.getImageFileName());
        statement.setInt(9, hotel.getCapacity());
    }

    private static HotelRoom mapHotel(ResultSet resultSet) throws SQLException {
        return new HotelRoom(
                resultSet.getLong("id"),
                resultSet.getLong("gestore_id"),
                resultSet.getString("nome"),
                resultSet.getString("citta"),
                resultSet.getString("tipo_camera"),
                resultSet.getString("servizi"),
                resultSet.getString("distanza_centro"),
                resultSet.getBigDecimal("prezzo_notte"),
                resultSet.getString("nome_immagine"),
                resultSet.getInt("capienza")
        );
    }
}
