package pl.sda.refactoring.service.port;

import pl.sda.refactoring.service.domain.Order;

public interface OrderRepoPort {

    void save(Order order);
}
