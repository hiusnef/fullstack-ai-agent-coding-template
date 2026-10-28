package io.hiusnef.sampleservice.config;

import io.hiusnef.sampleservice.shared.kernel.ddd.DomainEvent;
import io.hiusnef.sampleservice.shared.kernel.ddd.DomainEventPublisher;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publishes domain events as Spring application events, synchronously and in-process. Other contexts react with
 * {@code @EventListener} on the event type; swap for an outbox / message broker when events must cross processes.
 */
@Component
class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    SpringDomainEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(List<? extends DomainEvent> events) {
        events.forEach(publisher::publishEvent);
    }
}
