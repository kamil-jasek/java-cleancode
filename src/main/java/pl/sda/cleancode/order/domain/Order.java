package pl.sda.cleancode.order.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.Instant;

import static pl.sda.cleancode.order.domain.OrderStatus.CONFIRMED;

@EqualsAndHashCode
@ToString
@Getter
public final class Order {
    private final OrderId id;
    private final Instant createTime;
    private final OrderStatus status;
    private final CustomerId customerId;
    private final ExchangedOrderItemList orderItems;
    private final Price discount;
    private final Price deliveryCost;

    public Order(@NonNull OrderId orderId,
                 @NonNull Instant createTime,
                 @NonNull CustomerId customerId,
                 @NonNull ExchangedOrderItemList orderItems,
                 @NonNull Price deliveryCost,
                 @NonNull Price discount) {
        this.id = orderId;
        this.createTime = createTime;
        this.customerId = customerId;
        this.discount = discount;
        this.deliveryCost = deliveryCost;
        this.orderItems = orderItems;
        this.status = CONFIRMED;
    }

    public Price totalPrice() {
        return orderItems.totalPrice().plus(deliveryCost);
    }

    public Price discountedTotalPrice() {
        return orderItems.totalPrice().minus(discount).plus(deliveryCost);
    }
}

