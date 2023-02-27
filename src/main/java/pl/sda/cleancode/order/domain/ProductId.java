package pl.sda.cleancode.order.domain;

import java.util.UUID;

import static pl.sda.cleancode.application.util.UUIDExtension.uuid;

public record ProductId(UUID id) {

    public static ProductId of(String uuid) {
        return new ProductId(uuid(uuid));
    }
}
