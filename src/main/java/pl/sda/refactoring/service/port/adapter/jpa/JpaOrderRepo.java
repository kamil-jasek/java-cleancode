package pl.sda.refactoring.service.port.adapter.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sda.refactoring.service.domain.Order;
import pl.sda.refactoring.service.port.OrderRepoPort;

import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
final class JpaOrderRepo implements OrderRepoPort {

    private final OrderRepo orderRepo;

    @Override
    public void save(OrderEntity orderEntity) {
        orderRepo.save(orderEntity);
    }

    @Override
    public void save(Order order) {
        orderRepo.save(toEntity(order));
    }

    private OrderEntity toEntity(Order order) {
        return new OrderEntity(
            order.orderId().id(),
            order.createTime().time(),
            order.status(),
            order.customerId().value(),
            order.exchangedItems().baseCurrency(),
            order.exchangedItems().exchangedItems().stream()
                .map(item -> new OrderItemEntity(
                    UUID.randomUUID(),
                    item.item().productId().value(),
                    item.item().price().value(),
                    item.item().price().currency(),
                    item.item().weight().value(),
                    item.item().weight().unit(),
                    item.item().quantity().value(),
                    item.exchangedPrice().value()
                ))
                .collect(toList()),
            order.exchangedItems().totalPrice().value(),
            order.discountPrice().value(),
            order.exchangedItems().totalPrice().add(order.deliveryPrice())
                .minus(order.discountPrice()).value(),
            order.deliveryPrice().value()
        );
    }
}
