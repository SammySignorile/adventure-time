package org.example.adventuretime.bean;

import org.example.adventuretime.model.BookingStatus;
import org.example.adventuretime.model.ExtraService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bean di output usato per mostrare una prenotazione.
 * Non contiene la validazione di HotelBean: quella appartiene a HotelBean.
 */
@SuppressWarnings("java:S107")
public class BookingBean {

    private long id;
    private long userId;
    private HotelBean hotel;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int people;
    private BigDecimal totalPrice;
    private EnumSet<ExtraService> extras;
    private int pointsUsed;
    private BookingStatus status;

    public BookingBean(
            long id,
            long userId,
            HotelBean hotel,
            LocalDate checkIn,
            LocalDate checkOut,
            int people,
            BigDecimal totalPrice,
            Set<ExtraService> extras,
            int pointsUsed,
            BookingStatus status
    ) {
        this.id = id;
        this.userId = userId;
        this.hotel = hotel;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.people = people;
        this.totalPrice = totalPrice;
        this.extras = copyExtras(extras);
        this.pointsUsed = pointsUsed;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public HotelBean getHotel() {
        return hotel;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public int getPeople() {
        return people;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Set<ExtraService> getExtras() {
        return copyExtras(extras);
    }

    public int getPointsUsed() {
        return pointsUsed;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public String getExtrasLabel() {
        if (extras.isEmpty()) {
            return "Nessun servizio extra";
        }

        return extras.stream()
                .map(ExtraService::getDescription)
                .collect(Collectors.joining(", "));
    }

    private static EnumSet<ExtraService> copyExtras(
            Set<ExtraService> source
    ) {
        if (source == null || source.isEmpty()) {
            return EnumSet.noneOf(ExtraService.class);
        }
        return EnumSet.copyOf(source);
    }
}