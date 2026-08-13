package org.example.adventuretime.model;

import java.math.BigDecimal;

public enum ExtraService {
    CANCELLATION_INSURANCE("Assicurazione annullamento", new BigDecimal("80.00")),
    HEALTH_INSURANCE("Assicurazione medica", new BigDecimal("50.00")),
    FLEXIBLE_DATE("Cambio data flessibile", new BigDecimal("40.00"));

    private final String description;
    private final BigDecimal price;

    ExtraService(String description, BigDecimal price) {
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
