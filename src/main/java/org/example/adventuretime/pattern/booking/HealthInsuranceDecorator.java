package org.example.adventuretime.pattern.booking;

import org.example.adventuretime.model.ExtraService;

public final class HealthInsuranceDecorator extends BookingPriceDecorator {
    public HealthInsuranceDecorator(BookingPriceComponent wrapped) {
        super(wrapped, ExtraService.HEALTH_INSURANCE);
    }
}
