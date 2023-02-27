package pl.sda.cleancode.application.event;

import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

public record DomainEvent(
    @NonNull UUID eventId,
    @NonNull Instant eventTime,
    UUID correlationId,
    @NonNull DomainEventData data
) {

}
