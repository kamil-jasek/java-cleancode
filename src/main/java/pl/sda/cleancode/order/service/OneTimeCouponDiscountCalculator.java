package pl.sda.cleancode.order.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.sda.cleancode.order.infra.port.DiscountPort;
import pl.sda.cleancode.order.domain.Price;

import static java.math.BigDecimal.ZERO;

@RequiredArgsConstructor
final class OneTimeCouponDiscountCalculator implements DiscountCalculator {

    private final @NonNull DiscountPort couponPort;

    @Override
    public DiscountContext calculate(DiscountContext discountContext) {
        if (discountContext.discount()
            .isHigherThan(new Price(ZERO, discountContext.orderItems().baseCurrency()))) {
            return discountContext;
        }
        return discountContext.discountCoupon()
            .flatMap(coupon -> couponPort.deactivateCoupon(discountContext.customerId(), coupon))
            .map(discount -> discountContext.applyDiscount(discountContext.orderItems()
                .totalPrice()
                .multiply(discount.percentage().toDecimal())))
            .orElse(discountContext);
    }
}
