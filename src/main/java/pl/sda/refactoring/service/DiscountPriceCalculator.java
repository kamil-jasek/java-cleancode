package pl.sda.refactoring.service;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import pl.sda.refactoring.service.port.DiscountPort;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.math.RoundingMode.HALF_UP;

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

    BigDecimal calculateDiscountPrice(BigDecimal totalPrice,
                                      BigDecimal deliveryPrice,
                                      String coupon,
                                      @NonNull UUID customerId) {
        var discountPrice = BigDecimal.ZERO;
        if (coupon != null) {
            var discount = discountPort.getDiscount(coupon);
            if (discount != null) {
                discountPrice = totalPrice
                    .multiply(BigDecimal.valueOf(discount.value()))
                    .setScale(2, HALF_UP);
                discountPort.deactivate(coupon, customerId);
            }
        }
        if (freeDeliveryDays.contains(LocalDate.now(clock))) {
            discountPrice = discountPrice.add(deliveryPrice);
        }
        return discountPrice;
    }
}