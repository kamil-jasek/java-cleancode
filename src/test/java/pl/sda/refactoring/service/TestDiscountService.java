package pl.sda.refactoring.service;

import pl.sda.refactoring.service.port.DiscountPort;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

final class TestDiscountService implements DiscountPort {

    private final Map<String, Discount> discounts = new HashMap<>(Map.of(
        "ABC20", new Discount(0.1)
    ));

    @Override
    public Discount getDiscount(String coupon) {
        return discounts.get(coupon);
    }

    @Override
    public void deactivate(String coupon, UUID customerId) {
        discounts.remove(coupon);
    }
}
