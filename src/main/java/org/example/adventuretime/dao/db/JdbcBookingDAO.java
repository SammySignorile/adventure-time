package org.example.adventuretime.dao.db;

import org.example.adventuretime.dao.BookingDAO;
import org.example.adventuretime.exception.PersistenceException;
import org.example.adventuretime.model.Booking;
import org.example.adventuretime.model.BookingStatus;
import org.example.adventuretime.model.ExtraService;
import org.example.adventuretime.model.PaymentData;

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
                    prezzo_totale, extras, punti_usati, stato,
                    payment_token, card_holder, card_last_four,
                    payment_completed
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connectionManager.getConnection()
                .prepareStatement(
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
            PaymentData paymentData = booking.getPaymentData();
            statement.setString(10, paymentData.getToken());
            statement.setString(11, paymentData.getCardHolder());
            statement.setString(12, paymentData.getLastFourDigits());
            statement.setBoolean(13, booking.isPaymentCompleted());
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
                       prezzo_totale, extras, punti_usati, stato,
                       payment_token, card_holder, card_last_four,
                       payment_completed
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
                       b.punti_usati, b.stato, b.payment_token,
                       b.card_holder, b.card_last_four, b.payment_completed
                FROM bookings b
                JOIN hotelrooms h ON h.id = b.hotel_id
                WHERE h.gestore_id = ?
                ORDER BY b.check_in DESC
                """;
        return executeBookingListQuery(sql, managerId);
    }

    @Override
    public void updateStatus(long bookingId, BookingStatus status)
            throws PersistenceException {
        String sql = "UPDATE bookings SET stato = ? WHERE id = ?";

        try (PreparedStatement statement = connectionManager.getConnection()
                .prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setLong(2, bookingId);
            if (statement.executeUpdate() == 0) {
                throw new PersistenceException(
                        "La prenotazione selezionata non esiste.");
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante l'aggiornamento della prenotazione.", e);
        }
    }

    @Override
    public void approveBooking(long bookingId) throws PersistenceException {
        String sql = """
                UPDATE bookings
                SET stato = 'CONFIRMED', payment_completed = TRUE
                WHERE id = ? AND stato = 'PENDING_APPROVAL'
                """;
        try (PreparedStatement statement = connectionManager.getConnection()
                .prepareStatement(sql)) {
            statement.setLong(1, bookingId);
            if (statement.executeUpdate() == 0) {
                throw new PersistenceException(
                        "La richiesta non esiste o non e piu in attesa.");
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Errore durante l'approvazione della prenotazione.", e);
        }
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
                  AND stato IN ('PENDING_APPROVAL', 'CONFIRMED')
                  AND check_in < ?
                  AND check_out > ?
                """;

        try (PreparedStatement statement = connectionManager.getConnection()
                .prepareStatement(sql)) {
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
        try (PreparedStatement statement = connectionManager.getConnection()
                .prepareStatement(sql)) {
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
        Booking booking = new Booking();
        booking.setId(resultSet.getLong("id"));
        booking.setUserId(resultSet.getLong("user_id"));
        booking.setHotelId(resultSet.getLong("hotel_id"));
        booking.setCheckIn(resultSet.getDate("check_in").toLocalDate());
        booking.setCheckOut(resultSet.getDate("check_out").toLocalDate());
        booking.setPeople(resultSet.getInt("persone"));
        booking.setTotalPrice(resultSet.getBigDecimal("prezzo_totale"));
        booking.setExtras(deserializeExtras(resultSet.getString("extras")));
        booking.setPointsUsed(resultSet.getInt("punti_usati"));
        booking.setStatus(BookingStatus.valueOf(
                resultSet.getString("stato")));
        booking.setPaymentData(new PaymentData(
                resultSet.getString("payment_token"),
                resultSet.getString("card_holder"),
                resultSet.getString("card_last_four")
        ));
        booking.setPaymentCompleted(
                resultSet.getBoolean("payment_completed"));
        return booking;
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
