package pl.sda.cleancode.order.infra.port.adapter.customer;

import pl.sda.cleancode.order.domain.CustomerId;
import pl.sda.cleancode.order.infra.port.CustomerPort;

import java.util.List;

import static pl.sda.cleancode.application.util.UUIDExtension.uuid;

final class DummyCustomerPortAdapter implements CustomerPort {

    private final List<CustomerId> existingCustomers = List.of(
        new CustomerId(uuid("9c74dd58-83e3-4a21-8220-bec2ddcd590a")),
        new CustomerId(uuid("9905a3c3-ecf9-4445-8757-934f932e347b"))
    );

    @Override
    public boolean customerExists(CustomerId customerId) {
        return existingCustomers.contains(customerId);
    }
}
