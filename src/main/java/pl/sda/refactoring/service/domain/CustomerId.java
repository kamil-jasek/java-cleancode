package pl.sda.refactoring.service.domain;

import java.util.UUID;

public record CustomerId(UUID value) {

    public static CustomerId of(String uuid) {
        return new CustomerId(UUID.fromString(uuid));
    }
}
