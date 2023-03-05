package pl.sda.refactoring.service;

import lombok.NonNull;
import pl.sda.refactoring.service.domain.CustomerId;
import pl.sda.refactoring.service.domain.Price;

record DiscountContext(
    @NonNull Price currentDiscount,
    @NonNull CustomerId customerId,
    @NonNull Price totalPrice,
    @NonNull Price deliveryPrice,
    String coupon
) {
    public DiscountContext(CustomerId customerId,
                           Price totalPrice,
                           Price deliveryPrice,
                           String coupon) {
        this(Price.of("0.00", totalPrice.currency()),
            customerId,
            totalPrice,
            deliveryPrice,
            coupon);
    }

    public DiscountContext withCurrentPrice(Price currentDiscount) {
        return new DiscountContext(
            currentDiscount,
            customerId,
            totalPrice,
            deliveryPrice,
            coupon
        );
    }
}