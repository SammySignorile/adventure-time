package org.example.adventuretime.bean;

import org.example.adventuretime.model.ExtraService;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Set;

/**
 * Bean di output del preventivo. È costruito dalla BookingFacade e non riceve
 * direttamente testo libero dall'utente.
 */
@SuppressWarnings("java:S107")
public class BookingQuoteBean {

    private HotelBean hotel;
    private long nights;
    private BigDecimal basePrice;
    private EnumSet<ExtraService> extras;
    private BigDecimal extrasPrice;
    private int pointsUsed;
    private BigDecimal pointsDiscount;
    private BigDecimal totalPrice;

    public BookingQuoteBean(
            HotelBean hotel,
            long nights,
            BigDecimal basePrice,
            Set<ExtraService> extras,
            BigDecimal extrasPrice,
            int pointsUsed,
            BigDecimal pointsDiscount,
            BigDecimal totalPrice
    ) {
        this.hotel = hotel;
        this.nights = nights;
        this.basePrice = basePrice;
        this.extras = copyExtras(extras);
        this.extrasPrice = extrasPrice;
        this.pointsUsed = pointsUsed;
        this.pointsDiscount = pointsDiscount;
        this.totalPrice = totalPrice;
    }

    public HotelBean getHotel() {
        return hotel;
    }

    public long getNights() {
        return nights;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public Set<ExtraService> getExtras() {
        return copyExtras(extras);
    }

    public BigDecimal getExtrasPrice() {
        return extrasPrice;
    }

    public int getPointsUsed() {
        return pointsUsed;
    }

    public BigDecimal getPointsDiscount() {
        return pointsDiscount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
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