package org.example.adventuretime.pattern.booking;

import org.example.adventuretime.model.ExtraService;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Set;

public abstract class BookingPriceDecorator implements BookingPriceComponent {

    private final BookingPriceComponent wrapped;
    private final ExtraService extraService;

    protected BookingPriceDecorator(
            BookingPriceComponent wrapped,
            ExtraService extraService
    ) {
        this.wrapped = wrapped;
        this.extraService = extraService;
    }

    @Override
    public BigDecimal getPrice() {
        return wrapped.getPrice().add(extraService.getPrice());
    }

    @Override
    public Set<ExtraService> getExtras() {
        EnumSet<ExtraService> result = wrapped.getExtras().isEmpty()
                ? EnumSet.noneOf(ExtraService.class)
                : EnumSet.copyOf(wrapped.getExtras());
        result.add(extraService);
        return result;
    }
}
