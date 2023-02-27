package pl.sda.cleancode.order.service;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.sda.cleancode.order.domain.CustomerId;
import pl.sda.cleancode.order.domain.DiscountCoupon;
import pl.sda.cleancode.order.domain.ExchangedOrderItemList;
import pl.sda.cleancode.order.domain.Price;

import java.util.Optional;

import static java.math.BigDecimal.ZERO;

interface DiscountCalculator {
    DiscountContext calculate(DiscountContext discountContext);

    @Getter
    @RequiredArgsConstructor
    final class DiscountContext {
        private final CustomerId customerId;
        private final ExchangedOrderItemList orderItems;
        private final Price deliveryCost;
        private final Optional<DiscountCoupon> discountCoupon;
        private final Price discount;

        DiscountContext(@NonNull CustomerId customerId,
                        @NonNull ExchangedOrderItemList orderItems,
                        @NonNull Price deliveryCost,
                        @NonNull Optional<DiscountCoupon> discountCoupon) {
            this.customerId = customerId;
            this.orderItems = orderItems;
            this.deliveryCost = deliveryCost;
            this.discountCoupon = discountCoupon;
            this.discount = new Price(ZERO, orderItems.baseCurrency());
        }

        public DiscountContext applyDiscount(Price discount) {
            return new DiscountContext(
                customerId,
                orderItems,
                deliveryCost,
                discountCoupon,
                discount);
        }
    }
}
