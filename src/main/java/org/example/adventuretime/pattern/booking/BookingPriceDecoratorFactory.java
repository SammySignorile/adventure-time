package org.example.adventuretime.pattern.booking;

import org.example.adventuretime.model.ExtraService;

import java.util.Set;

/**
 * Keeps construction of the decorator chain in one place.
 */
public final class BookingPriceDecoratorFactory {

    private BookingPriceDecoratorFactory() {
    }

    public static BookingPriceComponent decorate(
            BookingPriceComponent base,
            Set<ExtraService> extras
    ) {
        BookingPriceComponent component = base;
        if (extras.contains(ExtraService.CANCELLATION_INSURANCE)) {
            component = new CancellationInsuranceDecorator(component);
        }
        if (extras.contains(ExtraService.HEALTH_INSURANCE)) {
            component = new HealthInsuranceDecorator(component);
        }
        if (extras.contains(ExtraService.FLEXIBLE_DATE)) {
            component = new FlexibleDateDecorator(component);
        }
        return component;
    }
}
