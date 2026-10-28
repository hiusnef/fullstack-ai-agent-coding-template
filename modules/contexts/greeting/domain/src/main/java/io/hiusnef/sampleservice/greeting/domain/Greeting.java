package io.hiusnef.sampleservice.greeting.domain;

import io.hiusnef.sampleservice.shared.kernel.ddd.ValueObject;
import java.util.Objects;

/** The greeting a guest receives. */
public record Greeting(PersonName recipient) implements ValueObject {

    public Greeting {
        Objects.requireNonNull(recipient, "recipient");
    }

    public String message() {
        return "Hello, %s!".formatted(recipient.value());
    }
}
