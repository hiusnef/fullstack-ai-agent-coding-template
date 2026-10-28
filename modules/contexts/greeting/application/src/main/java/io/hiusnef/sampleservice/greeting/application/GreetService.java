package io.hiusnef.sampleservice.greeting.application;

import io.hiusnef.sampleservice.greeting.application.port.in.GreetUseCase;
import io.hiusnef.sampleservice.greeting.domain.Greeting;
import io.hiusnef.sampleservice.greeting.domain.Guest;
import io.hiusnef.sampleservice.greeting.domain.GuestRepository;
import io.hiusnef.sampleservice.greeting.domain.PersonName;
import io.hiusnef.sampleservice.shared.kernel.ddd.DomainEventPublisher;
import io.hiusnef.sampleservice.shared.kernel.error.NotFoundException;
import java.time.Clock;

/** Orchestrates one use case: load the aggregate, let it decide, save it, publish what happened. No business rules here. */
public class GreetService implements GreetUseCase {

    private final GuestRepository guests;
    private final DomainEventPublisher events;
    private final Clock clock;

    public GreetService(GuestRepository guests, DomainEventPublisher events, Clock clock) {
        this.guests = guests;
        this.events = events;
        this.clock = clock;
    }

    @Override
    public Greeting greet(String name) {
        PersonName personName = new PersonName(name);
        Guest guest = guests.findByName(personName)
                .orElseThrow(() -> new NotFoundException("No greeting for name '%s'".formatted(name)));
        Greeting greeting = guest.greet(clock.instant());
        guests.save(guest);
        events.publish(guest.pullDomainEvents());
        return greeting;
    }
}
