package org.example.adventuretime.bean;

import org.example.adventuretime.model.ExtraService;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Set;

/**
 * Bean di output del preventivo. È costruito dalla BookingFacade e non riceve
 * direttamente testo libero dall'utente.
 */
public class BookingQuoteBean {

    private HotelBean hotel;
    private long nights;
    private BigDecimal basePrice;
    private EnumSet<ExtraService> extras =
            EnumSet.noneOf(ExtraService.class);
    private BigDecimal extrasPrice;
    private int pointsUsed;
    private BigDecimal pointsDiscount;
    private BigDecimal totalPrice;

    public BookingQuoteBean() {
        // Costruttore vuoto da Java Bean; la Facade valorizza i campi.
    }

    public HotelBean getHotel() {
        return hotel;
    }

    public void setHotel(HotelBean hotel) {
        this.hotel = hotel;
    }

    public long getNights() {
        return nights;
    }

    public void setNights(long nights) {
        this.nights = nights;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Set<ExtraService> getExtras() {
        return copyExtras(extras);
    }

    public void setExtras(Set<ExtraService> extras) {
        this.extras = copyExtras(extras);
    }

    public BigDecimal getExtrasPrice() {
        return extrasPrice;
    }

    public void setExtrasPrice(BigDecimal extrasPrice) {
        this.extrasPrice = extrasPrice;
    }

    public int getPointsUsed() {
        return pointsUsed;
    }

    public void setPointsUsed(int pointsUsed) {
        this.pointsUsed = pointsUsed;
    }

    public BigDecimal getPointsDiscount() {
        return pointsDiscount;
    }

    public void setPointsDiscount(BigDecimal pointsDiscount) {
        this.pointsDiscount = pointsDiscount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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
