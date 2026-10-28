package io.hiusnef.sampleservice.shared.kernel.ddd;

import java.time.Instant;

/** Something that happened in the domain, named in the past tense (e.g. {@code GuestGreeted}). Immutable. */
public interface DomainEvent {

    Instant occurredOn();
}
