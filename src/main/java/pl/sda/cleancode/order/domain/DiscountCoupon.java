package pl.sda.cleancode.order.domain;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

public record DiscountCoupon(String coupon) {

    public static Optional<DiscountCoupon> none() {
        return empty();
    }

    public static Optional<DiscountCoupon> of(String coupon) {
        return ofNullable(coupon).map(DiscountCoupon::new);
    }
}
