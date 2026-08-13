package org.example.adventuretime.pattern.booking;

import org.example.adventuretime.model.ExtraService;

public final class CancellationInsuranceDecorator extends BookingPriceDecorator {
    public CancellationInsuranceDecorator(BookingPriceComponent wrapped) {
        super(wrapped, ExtraService.CANCELLATION_INSURANCE);
    }
}
