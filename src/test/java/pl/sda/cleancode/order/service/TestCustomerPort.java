package pl.sda.cleancode.order.service;

import pl.sda.cleancode.order.infra.port.CustomerPort;
import pl.sda.cleancode.order.domain.CustomerId;

import java.util.List;

import static pl.sda.cleancode.application.util.UUIDExtension.uuid;

final class TestCustomerPort implements CustomerPort {

    private final List<CustomerId> existingCustomers = List.of(
        new CustomerId(uuid("9c74dd58-83e3-4a21-8220-bec2ddcd590a"))
    );

    @Override
    public boolean customerExists(CustomerId customerId) {
        return existingCustomers.contains(customerId);
    }
}
