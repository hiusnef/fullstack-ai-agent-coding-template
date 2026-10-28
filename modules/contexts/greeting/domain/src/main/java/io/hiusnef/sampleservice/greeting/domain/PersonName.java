package io.hiusnef.sampleservice.greeting.domain;

import static io.hiusnef.sampleservice.shared.kernel.util.Preconditions.requireNonBlank;

import io.hiusnef.sampleservice.shared.kernel.ddd.ValueObject;
import java.util.Locale;
import java.util.regex.Pattern;

/** A person's name: 1-50 latin letters. Two names are the same person regardless of case. */
public record PersonName(String value) implements ValueObject {

    private static final Pattern VALID = Pattern.compile("[A-Za-z]{1,50}");

    public PersonName {
        requireNonBlank(value, "name");
        if (!VALID.matcher(value).matches()) {
            throw new IllegalArgumentException("name must be 1-50 latin letters");
        }
    }

    /** Case-insensitive identity, e.g. for lookups. */
    public String normalized() {
        return value.toLowerCase(Locale.ROOT);
    }
}
