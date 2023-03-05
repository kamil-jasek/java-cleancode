package pl.sda.refactoring.service;

import pl.sda.refactoring.service.port.adapter.jpa.OrderEntity;
import pl.sda.refactoring.service.domain.Order;
import pl.sda.refactoring.service.port.OrderRepoPort;

final class TestOrderRepo implements OrderRepoPort {

    @Override
    public void save(OrderEntity orderEntity) {

    }

    @Override
    public void save(Order order) {

    }
}
