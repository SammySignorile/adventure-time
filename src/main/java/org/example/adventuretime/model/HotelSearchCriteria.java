package org.example.adventuretime.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Domain-level search criteria. UI beans are converted to this object by the
 * application controller so DAOs never depend on JavaFX or presentation types.
 */
public record HotelSearchCriteria(
        String city,
        LocalDate checkIn,
        LocalDate checkOut,
        int people,
        BigDecimal maximumPricePerNight
) {
}
