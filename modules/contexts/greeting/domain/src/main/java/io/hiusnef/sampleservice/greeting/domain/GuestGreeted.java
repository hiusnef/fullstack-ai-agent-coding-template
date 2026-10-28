package io.hiusnef.sampleservice.greeting.domain;

import io.hiusnef.sampleservice.shared.kernel.ddd.DomainEvent;
import java.time.Instant;

public record GuestGreeted(GuestId guestId, PersonName name, int timesGreeted, Instant occurredOn)
        implements DomainEvent {}
