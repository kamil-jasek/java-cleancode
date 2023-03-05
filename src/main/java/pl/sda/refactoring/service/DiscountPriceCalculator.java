package pl.sda.refactoring.service;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import pl.sda.refactoring.service.domain.CustomerId;
import pl.sda.refactoring.service.domain.Price;
import pl.sda.refactoring.service.port.DiscountPort;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Component
public class DiscountPriceCalculator {
    private final DiscountPort discountPort;
    private final List<LocalDate> freeDeliveryDays;
    private final Clock clock;

    DiscountPriceCalculator(@NonNull DiscountPort discountPort,
                            @NonNull OrderSettings settings,
                            @NonNull Clock clock) {
        this.discountPort = discountPort;
        this.freeDeliveryDays = settings.discountSettings().freeDeliveryDays();
        this.clock = clock;
    }

    Price calculateDiscountPrice(Price totalPrice,
                                 Price deliveryPrice,
                                 String coupon,
                                 @NonNull CustomerId customerId) {
        var discountPrice = Price.of("0.00", totalPrice.currency());
        if (coupon != null) {
            var discount = discountPort.getDiscount(coupon);
            if (discount != null) {
                discountPrice = totalPrice.multiply(BigDecimal.valueOf(discount.value()));
                discountPort.deactivate(coupon, customerId.value());
            }
        }
        if (freeDeliveryDays.contains(LocalDate.now(clock))) {
            discountPrice = discountPrice.add(deliveryPrice);
        }
        return discountPrice;
    }
}