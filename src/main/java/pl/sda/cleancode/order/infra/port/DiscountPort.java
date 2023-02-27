package pl.sda.cleancode.order.infra.port;

import pl.sda.cleancode.order.domain.CustomerId;
import pl.sda.cleancode.order.domain.DiscountCoupon;

import java.math.BigDecimal;
import java.util.Optional;

import static java.math.RoundingMode.HALF_UP;
import static pl.sda.cleancode.application.util.DomainArgumentCheck.check;

public interface DiscountPort {

    Optional<Discount> deactivateCoupon(CustomerId customerId, DiscountCoupon coupon);

    record Discount(String code, DiscountPercentage percentage) {

        public record DiscountPercentage(int percentage) {

            public DiscountPercentage {
                check(percentage >= 0 && percentage <= 100,
                    "discount percentage must be between 0% and 100%");
            }

            public BigDecimal toDecimal() {
                return BigDecimal.valueOf(percentage)
                    .setScale(2, HALF_UP)
                    .divide(BigDecimal.valueOf(100), HALF_UP);
            }
        }
    }
}
