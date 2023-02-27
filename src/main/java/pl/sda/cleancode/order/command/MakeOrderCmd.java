package pl.sda.cleancode.order.command;

import lombok.NonNull;
import pl.sda.cleancode.order.domain.Currency;
import pl.sda.cleancode.order.domain.CustomerId;
import pl.sda.cleancode.order.domain.DiscountCoupon;
import pl.sda.cleancode.order.domain.OrderItem;

import java.util.List;
import java.util.Optional;

public record MakeOrderCmd(
    @NonNull CustomerId customerId,
    @NonNull List<OrderItem> orderItemList,
    @NonNull Currency orderCurrency,
    @NonNull Optional<DiscountCoupon> discountCoupon
) {
}
