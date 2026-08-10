package org.example.adventuretime.pattern.booking;

import org.example.adventuretime.model.ExtraService;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Set;

public final class BaseBookingPrice implements BookingPriceComponent {

    private final BigDecimal basePrice;

    public BaseBookingPrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public BigDecimal getPrice() {
        return basePrice;
    }

    @Override
    public Set<ExtraService> getExtras() {
        return EnumSet.noneOf(ExtraService.class);
    }
}
