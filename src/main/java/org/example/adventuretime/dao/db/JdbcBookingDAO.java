package org.example.adventuretime.dao.db;

import org.example.adventuretime.dao.BookingDAO;
import org.example.adventuretime.exception.PersistenceException;
import org.example.adventuretime.model.Booking;
import org.example.adventuretime.model.BookingStatus;
import org.example.adventuretime.model.ExtraService;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class JdbcBookingDAO implements BookingDAO {

    private final DBConnectionManager connectionManager;

    public JdbcBookingDAO(DBConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Booking save(Booking booking) throws PersistenceException {
        String sql = """
                INSERT INTO bookings(
                    user_id, hotel_id, check_in, check_out, persone,
                    prezzo_totale, extras, punti_usati, stato
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, booking.getUserId());
            statement.setLong(2, booking.getHotelId());
            statement.setDate(3, Date.valueOf(booking.getCheckIn()));
            statement.setDate(4, Date.valueOf(booking.getCheckOut()));
            statement.setInt(5, booking.getPeople());
            statement.setBigDecimal(6, booking.getTotalPrice());
            statement.setString(7, serializeExtras(booking.getExtras()));
            statement.setInt(8, booking.getPointsUsed());
            statement.setString(9, booking.getStatus().name());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new PersistenceException(
                            "Il database non ha restituito l'id della prenotazione.");
                }
                Booking created = new Booking(booking);
                created.setId(keys.getLong(1));
                return created;
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante il salvataggio della prenotazione.", e);
        }
    }

    @Override
    public List<Booking> findByUserId(long userId)
            throws PersistenceException {
        String sql = """
                SELECT id, user_id, hotel_id, check_in, check_out, persone,
                       prezzo_totale, extras, punti_usati, stato
                FROM bookings
                WHERE user_id = ?
                ORDER BY check_in DESC
                """;
        return executeBookingListQuery(sql, userId);
    }

    @Override
    public List<Booking> findByManagerId(long managerId)
            throws PersistenceException {
        String sql = """
                SELECT b.id, b.user_id, b.hotel_id, b.check_in, b.check_out,
                       b.persone, b.prezzo_totale, b.extras,
                       b.punti_usati, b.stato
                FROM bookings b
                JOIN hotelrooms h ON h.id = b.hotel_id
                WHERE h.gestore_id = ?
                ORDER BY b.check_in DESC
                """;
        return executeBookingListQuery(sql, managerId);
    }

    @Override
    public boolean isHotelAvailable(
            long hotelId,
            LocalDate checkIn,
            LocalDate checkOut
    ) throws PersistenceException {
        String sql = """
                SELECT COUNT(*) AS conflicts
                FROM bookings
                WHERE hotel_id = ?
                  AND stato = 'CONFIRMED'
                  AND check_in < ?
                  AND check_out > ?
                """;

        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, hotelId);
            statement.setDate(2, Date.valueOf(checkOut));
            statement.setDate(3, Date.valueOf(checkIn));
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt("conflicts") == 0;
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante il controllo disponibilità.", e);
        }
    }

    private List<Booking> executeBookingListQuery(
            String sql,
            long parameter
    ) throws PersistenceException {
        List<Booking> bookings = new ArrayList<>();
        try (Connection connection = connectionManager.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, parameter);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bookings.add(mapBooking(resultSet));
                }
            }
            return bookings;
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante la lettura delle prenotazioni.", e);
        }
    }

    private static Booking mapBooking(ResultSet resultSet)
            throws SQLException {
        return new Booking(
                resultSet.getLong("id"),
                resultSet.getLong("user_id"),
                resultSet.getLong("hotel_id"),
                resultSet.getDate("check_in").toLocalDate(),
                resultSet.getDate("check_out").toLocalDate(),
                resultSet.getInt("persone"),
                resultSet.getBigDecimal("prezzo_totale"),
                deserializeExtras(resultSet.getString("extras")),
                resultSet.getInt("punti_usati"),
                BookingStatus.valueOf(resultSet.getString("stato"))
        );
    }

    private static String serializeExtras(Set<ExtraService> extras) {
        return extras.stream()
                .map(Enum::name)
                .sorted()
                .collect(Collectors.joining(","));
    }

    private static Set<ExtraService> deserializeExtras(String text) {
        if (text == null || text.isBlank()) {
            return EnumSet.noneOf(ExtraService.class);
        }
        EnumSet<ExtraService> result = EnumSet.noneOf(ExtraService.class);
        for (String token : text.split(",")) {
            result.add(ExtraService.valueOf(token));
        }
        return result;
    }
}
