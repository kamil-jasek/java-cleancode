package pl.sda.refactoring.service.port;

import java.util.UUID;

public interface DiscountPort {
    Discount getDiscount(String coupon);

    void deactivate(String coupon, UUID customerId);

    record Discount(double value) {
    }
}
