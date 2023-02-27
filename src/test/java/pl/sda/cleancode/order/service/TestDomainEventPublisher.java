package pl.sda.cleancode.order.service;

import pl.sda.cleancode.application.event.DomainEvent;
import pl.sda.cleancode.application.event.DomainEventData;
import pl.sda.cleancode.application.event.DomainEventPublisher;

import java.util.ArrayList;
import java.util.List;

final class TestDomainEventPublisher implements DomainEventPublisher {

    private final List<DomainEvent> publishedEvents = new ArrayList<>();

    @Override
    public void publish(DomainEvent event) {
        publishedEvents.add(event);
    }

    public boolean hasPublished(Class<? extends DomainEventData> eventDataClass) {
        return publishedEvents
            .stream()
            .anyMatch(domainEvent -> domainEvent.data()
                .getClass()
                .isAssignableFrom(eventDataClass));
    }
}
