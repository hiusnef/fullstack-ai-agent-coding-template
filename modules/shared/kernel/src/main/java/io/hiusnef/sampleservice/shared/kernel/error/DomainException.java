package io.hiusnef.sampleservice.shared.kernel.error;

import java.util.Objects;

/** Base class for business-rule violations; adapters translate them using {@link #code()}. */
public abstract class DomainException extends RuntimeException {

    private final ErrorCode code;

    protected DomainException(ErrorCode code, String message) {
        super(message);
        this.code = Objects.requireNonNull(code, "code");
    }

    public ErrorCode code() {
        return code;
    }
}
