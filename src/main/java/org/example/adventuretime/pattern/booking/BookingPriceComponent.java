package org.example.adventuretime.pattern.booking;

import org.example.adventuretime.model.ExtraService;

import java.math.BigDecimal;
import java.util.Set;

public interface BookingPriceComponent {
    BigDecimal getPrice();

    Set<ExtraService> getExtras();
}
