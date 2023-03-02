package pl.sda.cleancode.application.event;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class DomainEvent<T extends DomainEventData> {
    private final @NonNull UUID eventId;
    private final @NonNull Instant eventTime;
    private final UUID correlationId;
    private final @NonNull T data;
}
