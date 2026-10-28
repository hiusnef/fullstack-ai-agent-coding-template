package io.hiusnef.sampleservice.shared.kernel.ddd;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Consistency boundary and the only entry point to its entities. Records domain events while it changes; the
 * application layer pulls them after saving and hands them to a {@link DomainEventPublisher}.
 */
public abstract class AggregateRoot<ID> extends Entity<ID> {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected AggregateRoot(ID id) {
        super(id);
    }

    protected void registerEvent(DomainEvent event) {
        domainEvents.add(Objects.requireNonNull(event, "event"));
    }

    /** Returns the events recorded since the last call and forgets them. */
    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }
}
