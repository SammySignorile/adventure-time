package org.example.adventuretime.bean;

import org.example.adventuretime.model.BookingStatus;
import org.example.adventuretime.model.ExtraService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bean di output usato per mostrare una prenotazione.
 * Non contiene la validazione di HotelBean: quella appartiene a HotelBean.
 */
public class BookingBean {

    private long id;
    private long userId;
    private HotelBean hotel;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int people;
    private BigDecimal totalPrice;
    private EnumSet<ExtraService> extras =
            EnumSet.noneOf(ExtraService.class);
    private int pointsUsed;
    private BookingStatus status;

    public BookingBean() {
        // Costruttore vuoto da Java Bean; il mapper valorizza i campi.
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public HotelBean getHotel() {
        return hotel;
    }

    public void setHotel(HotelBean hotel) {
        this.hotel = hotel;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = Objects.requireNonNull(
                checkIn, "La data di arrivo è obbligatoria.");
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = Objects.requireNonNull(
                checkOut, "La data di partenza è obbligatoria.");
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = Objects.requireNonNull(
                totalPrice, "Il prezzo totale è obbligatorio.");
    }

    public Set<ExtraService> getExtras() {
        return copyExtras(extras);
    }

    public void setExtras(Set<ExtraService> extras) {
        this.extras = copyExtras(extras);
    }

    public int getPointsUsed() {
        return pointsUsed;
    }

    public void setPointsUsed(int pointsUsed) {
        this.pointsUsed = pointsUsed;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
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
