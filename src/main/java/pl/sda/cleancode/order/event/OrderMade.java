package pl.sda.cleancode.order.event;

import lombok.NonNull;
import pl.sda.cleancode.application.event.DomainEventData;
import pl.sda.cleancode.order.domain.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public record OrderMade(
    @NonNull UUID id,
    @NonNull UUID customerId,
    @NonNull Instant createTime,
    @NonNull String status,
    @NonNull String currency,
    @NonNull List<OrderMadeItem> orderItems,
    @NonNull BigDecimal discount,
    @NonNull BigDecimal deliveryCost
) implements DomainEventData {

    public record OrderMadeItem(
        @NonNull UUID productId,
        @NonNull BigDecimal originalPrice,
        @NonNull String originalCurrency,
        @NonNull BigDecimal exchangedPrice,
        @NonNull double weight,
        @NonNull String weightUnit,
        int quantity
    ) {
    }

    public static OrderMade from(Order order) {
        return new OrderMade(
            order.id().id(),
            order.customerId().id(),
            order.createTime(),
            order.status().name(),
            order.orderItems().baseCurrency().name(),
            order.orderItems().orderItems()
                .stream()
                .map(item -> new OrderMadeItem(
                    item.orderItem().productId().id(),
                    item.orderItem().price().value(),
                    item.orderItem().price().currency().name(),
                    item.exchangedPrice().value(),
                    item.orderItem().weight().value(),
                    item.orderItem().weight().unit().name(),
                    item.orderItem().quantity().value()))
                .collect(toList()),
            order.discount().value(),
            order.deliveryCost().value()
        );
    }
}
