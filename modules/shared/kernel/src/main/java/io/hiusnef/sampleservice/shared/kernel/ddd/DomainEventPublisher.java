package io.hiusnef.sampleservice.shared.kernel.ddd;

import java.util.List;

/** Output port for domain events, shared by every bounded context; implemented in the composition root. */
public interface DomainEventPublisher {

    void publish(List<? extends DomainEvent> events);
}
