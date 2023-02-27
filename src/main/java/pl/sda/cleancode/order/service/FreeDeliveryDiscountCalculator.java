package pl.sda.cleancode.order.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
final class FreeDeliveryDiscountCalculator implements DiscountCalculator {

    private final @NonNull List<LocalDate> freeDeliveryDays;
    private final @NonNull Clock clock;

    @Override
    public DiscountContext calculate(DiscountContext discountContext) {
        if (!freeDeliveryDays.contains(LocalDate.now(clock))) {
            return discountContext;
        }
        return discountContext.applyDiscount(discountContext
            .discount()
            .plus(discountContext.deliveryCost()));
    }
}
