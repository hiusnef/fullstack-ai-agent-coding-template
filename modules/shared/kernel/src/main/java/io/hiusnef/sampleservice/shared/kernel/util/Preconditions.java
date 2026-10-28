package io.hiusnef.sampleservice.shared.kernel.util;

import java.util.Objects;

/** Argument checks for constructors and factories; throw {@link IllegalArgumentException} on violation. */
public final class Preconditions {

    private Preconditions() {}

    public static String requireNonBlank(String value, String name) {
        Objects.requireNonNull(value, name);
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }
}
