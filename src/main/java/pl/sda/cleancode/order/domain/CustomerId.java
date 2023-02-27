package pl.sda.cleancode.order.domain;

import java.util.UUID;

import static pl.sda.cleancode.application.util.UUIDExtension.uuid;

public record CustomerId(UUID id) {
    public static CustomerId of(String name) {
        return new CustomerId(uuid(name));
    }
}
