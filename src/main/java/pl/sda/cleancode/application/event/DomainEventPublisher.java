package pl.sda.cleancode.application.event;

public interface DomainEventPublisher {

    void publish(DomainEvent event);
}
