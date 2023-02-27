package pl.sda.cleancode.application.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DomainEventPublisherConfig {

    @Bean
    DomainEventPublisher domainEventPublisher(ApplicationEventPublisher eventPublisher) {
        return new SpringDomainEventPublisher(eventPublisher);
    }
}
