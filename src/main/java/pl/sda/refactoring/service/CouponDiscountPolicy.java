package pl.sda.refactoring.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.sda.refactoring.service.domain.Price;
import pl.sda.refactoring.service.port.DiscountPort;

import java.math.BigDecimal;

@RequiredArgsConstructor
final class CouponDiscountPolicy implements DiscountPolicy {

    private final @NonNull DiscountPort discountPort;

    @Override
    public Price calculate(DiscountContext ctx) {
        if (ctx.coupon() != null) {
            var discount = discountPort.getDiscount(ctx.coupon());
            if (discount != null) {
                discountPort.deactivate(ctx.coupon(), ctx.customerId().value());
                return ctx.currentDiscount()
                    .add(ctx.totalPrice().multiply(BigDecimal.valueOf(discount.value())));
            }
        }
        return ctx.currentDiscount();
    }
}
