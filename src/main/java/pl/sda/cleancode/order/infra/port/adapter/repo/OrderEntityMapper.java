package pl.sda.cleancode.order.infra.port.adapter.repo;

import pl.sda.cleancode.order.domain.*;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

final class OrderEntityMapper {

    OrderEntity toEntity(Order order) {
        return new OrderEntity(
            order.id().id(),
            order.customerId().id(),
            order.createTime(),
            order.status().name(),
            order.orderItems().baseCurrency().name(),
            mapOrderItemsToEntities(order),
            order.discount().value(),
            order.deliveryCost().value());
    }

    private static List<OrderItemEntity> mapOrderItemsToEntities(Order order) {
        return order.orderItems()
            .orderItems()
            .stream()
            .map(item -> new OrderItemEntity(
                UUID.randomUUID(),
                item.orderItem().productId().id(),
                item.orderItem().price().value(),
                item.orderItem().price().currency().name(),
                item.exchangedPrice().value(),
                item.orderItem().weight().value(),
                item.orderItem().weight().unit().name(),
                item.orderItem().quantity().value()))
            .collect(toList());
    }

    Order toDomain(OrderEntity entity) {
        final var orderCurrency = Currency.valueOf(entity.currency());
        return new Order(
            new OrderId(entity.id()),
            entity.createTime(),
            new CustomerId(entity.customerId()),
            new ExchangedOrderItemList(
                orderCurrency,
                mapOrderItemEntitiesToDomain(entity.orderItems(), orderCurrency)
            ),
            new Price(entity.deliveryCost(), orderCurrency),
            new Price(entity.discount(), orderCurrency));
    }

    private ExchangedOrderItem[] mapOrderItemEntitiesToDomain(List<OrderItemEntity> orderItems,
                                                              Currency orderCurrency) {
        return orderItems
            .stream()
            .map(item -> new ExchangedOrderItem(
                new OrderItem(
                    new ProductId(item.productId()),
                    new Price(item.originalPrice(), Currency.valueOf(item.originalCurrency())),
                    new Weight(item.weight(), WeightUnit.valueOf(item.weightUnit())),
                    Quantity.of(item.quantity())
                ),
                new Price(item.exchangedPrice(), orderCurrency)))
            .toArray(ExchangedOrderItem[]::new);
    }
}
