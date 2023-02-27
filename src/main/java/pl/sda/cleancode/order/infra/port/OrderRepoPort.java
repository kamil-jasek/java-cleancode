package pl.sda.cleancode.order.infra.port;

import pl.sda.cleancode.order.domain.Order;
import pl.sda.cleancode.order.domain.OrderId;

public interface OrderRepoPort {
    Order getOrder(OrderId orderId);

    void save(Order order);
}
