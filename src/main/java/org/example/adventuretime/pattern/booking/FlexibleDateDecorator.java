package org.example.adventuretime.pattern.booking;

import org.example.adventuretime.model.ExtraService;

public final class FlexibleDateDecorator extends BookingPriceDecorator {
    public FlexibleDateDecorator(BookingPriceComponent wrapped) {
        super(wrapped, ExtraService.FLEXIBLE_DATE);
    }
}
