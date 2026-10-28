package io.hiusnef.sampleservice.shared.kernel.error;

/**
 * Stable, protocol-neutral error categories. Adapters map each code to their protocol (HTTP status, message header...);
 * {@link #slug()} is part of public error identifiers (e.g. the RFC 9457 {@code type} URI) and must not change.
 */
public enum ErrorCode {
    NOT_FOUND("not-found"),
    CONFLICT("conflict");

    private final String slug;

    ErrorCode(String slug) {
        this.slug = slug;
    }

    public String slug() {
        return slug;
    }
}
