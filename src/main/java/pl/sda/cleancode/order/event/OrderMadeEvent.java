package pl.sda.cleancode.order.event;

import lombok.NonNull;
import pl.sda.cleancode.application.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public final class OrderMadeEvent extends DomainEvent<OrderMadeEventData> {

    public OrderMadeEvent(@NonNull UUID eventId,
                          @NonNull Instant eventTime,
                          UUID correlationId,
                          @NonNull OrderMadeEventData data) {
        super(eventId, eventTime, correlationId, data);
    }
}
