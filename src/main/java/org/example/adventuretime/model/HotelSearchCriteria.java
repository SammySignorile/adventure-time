package org.example.adventuretime.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HotelSearchCriteria(
        String city,
        LocalDate checkIn,
        LocalDate checkOut,
        int people,
        BigDecimal maximumPricePerNight
) {
}
