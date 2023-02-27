package pl.sda.cleancode.order.domain;

import java.util.UUID;

public record OrderId(UUID id) {
    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }

    public static OrderId of(String uuid) {
        return new OrderId(UUID.fromString(uuid));
    }
}
