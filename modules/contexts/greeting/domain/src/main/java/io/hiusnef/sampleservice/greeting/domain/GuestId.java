package io.hiusnef.sampleservice.greeting.domain;

import io.hiusnef.sampleservice.shared.kernel.ddd.ValueObject;
import java.util.Objects;
import java.util.UUID;

public record GuestId(UUID value) implements ValueObject {

    public GuestId {
        Objects.requireNonNull(value, "value");
    }

    public static GuestId newId() {
        return new GuestId(UUID.randomUUID());
    }
}
