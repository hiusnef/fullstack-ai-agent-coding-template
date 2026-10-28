package io.hiusnef.sampleservice.greeting.domain;

import io.hiusnef.sampleservice.shared.kernel.ddd.AggregateRoot;
import java.time.Instant;
import java.util.Objects;

/** Aggregate root: a known guest who can be greeted. Tracks how often they were greeted. */
public final class Guest extends AggregateRoot<GuestId> {

    private final PersonName name;
    private int timesGreeted;

    private Guest(GuestId id, PersonName name, int timesGreeted) {
        super(id);
        this.name = Objects.requireNonNull(name, "name");
        if (timesGreeted < 0) {
            throw new IllegalArgumentException("timesGreeted must not be negative");
        }
        this.timesGreeted = timesGreeted;
    }

    /** A new guest who has never been greeted. */
    public static Guest register(PersonName name) {
        return new Guest(GuestId.newId(), name, 0);
    }

    /** Rebuilds a guest from storage; only repositories should call this. */
    public static Guest reconstitute(GuestId id, PersonName name, int timesGreeted) {
        return new Guest(id, name, timesGreeted);
    }

    public Greeting greet(Instant now) {
        timesGreeted++;
        registerEvent(new GuestGreeted(id(), name, timesGreeted, now));
        return new Greeting(name);
    }

    public PersonName name() {
        return name;
    }

    public int timesGreeted() {
        return timesGreeted;
    }
}
