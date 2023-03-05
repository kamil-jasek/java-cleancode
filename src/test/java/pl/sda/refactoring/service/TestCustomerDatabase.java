package pl.sda.refactoring.service;

import pl.sda.refactoring.service.port.CustomerPort;

import java.util.List;
import java.util.UUID;

public final class TestCustomerDatabase implements CustomerPort {

    private final List<UUID> existingCustomers = List.of(
        UUID.fromString("f363c254-6bc3-440b-b120-4870999da0d9"),
        UUID.fromString("9c74dd58-83e3-4a21-8220-bec2ddcd590c")
    );

    @Override
    public boolean exists(UUID customerId) {
        return existingCustomers.contains(customerId);
    }
}
