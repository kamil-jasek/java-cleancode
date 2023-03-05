package pl.sda.refactoring.service.port;

import pl.sda.refactoring.service.port.adapter.jpa.OrderEntity;
import pl.sda.refactoring.service.domain.Order;

public interface OrderRepoPort {

    void save(OrderEntity orderEntity);

    void save(Order order);
}
