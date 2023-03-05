package pl.sda.refactoring.service;

import lombok.NonNull;
import pl.sda.refactoring.service.domain.Price;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;


final class FreeDeliveryDayDiscountPolicy implements DiscountPolicy {

    private final List<LocalDate> freeDeliveryDays;
    private final Clock clock;

    FreeDeliveryDayDiscountPolicy(@NonNull OrderSettings settings, @NonNull Clock clock) {
        this.freeDeliveryDays = settings.discountSettings().freeDeliveryDays();
        this.clock = clock;
    }

    @Override
    public Price calculate(DiscountContext context) {
        if (freeDeliveryDays.contains(LocalDate.now(clock))) {
            return context.currentDiscount().add(context.deliveryPrice());
        }
        return context.currentDiscount();
    }
}
