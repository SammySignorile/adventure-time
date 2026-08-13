package org.example.adventuretime.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public class Booking implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long id;
    private long userId;
    private long hotelId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int people;
    private BigDecimal totalPrice;
    private EnumSet<ExtraService> extras;
    private int pointsUsed;
    private BookingStatus status;
    private PaymentData paymentData;
    private boolean paymentCompleted;

    public Booking() {
        extras = EnumSet.noneOf(ExtraService.class);
        status = BookingStatus.PENDING_APPROVAL;
    }

    public Booking(Booking other) {
        this.id = other.id;
        this.userId = other.userId;
        this.hotelId = other.hotelId;
        this.checkIn = other.checkIn;
        this.checkOut = other.checkOut;
        this.people = other.people;
        this.totalPrice = other.totalPrice;
        this.extras = other.extras.isEmpty()
                ? EnumSet.noneOf(ExtraService.class)
                : EnumSet.copyOf(other.extras);
        this.pointsUsed = other.pointsUsed;
        this.status = other.status;
        this.paymentData = other.paymentData == null
                ? null
                : new PaymentData(other.paymentData);
        this.paymentCompleted = other.paymentCompleted;
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

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
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
        this.totalPrice = totalPrice;
    }

    public Set<ExtraService> getExtras() {
        return EnumSet.copyOf(extras);
    }

    public void setExtras(Set<ExtraService> extras) {
        this.extras = extras.isEmpty()
                ? EnumSet.noneOf(ExtraService.class)
                : EnumSet.copyOf(extras);
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

    public PaymentData getPaymentData() {
        return paymentData == null ? null : new PaymentData(paymentData);
    }

    public void setPaymentData(PaymentData paymentData) {
        this.paymentData = paymentData == null
                ? null
                : new PaymentData(paymentData);
    }

    public boolean isPaymentCompleted() {
        return paymentCompleted;
    }

    public void setPaymentCompleted(boolean paymentCompleted) {
        this.paymentCompleted = paymentCompleted;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Booking booking)) {
            return false;
        }
        return id == booking.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
