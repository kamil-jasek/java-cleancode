package pl.sda.cleancode.order.service;

import pl.sda.cleancode.order.domain.Order;
import pl.sda.cleancode.order.domain.OrderId;
import pl.sda.cleancode.order.infra.port.OrderRepoPort;

import java.util.HashMap;
import java.util.Map;

final class TestOrderRepoPort implements OrderRepoPort {

    private final Map<OrderId, Order> repo = new HashMap<>();

    @Override
    public Order getOrder(OrderId orderId) {
        return repo.get(orderId);
    }

    @Override
    public void save(Order order) {
        repo.put(order.id(), order);
    }
}
