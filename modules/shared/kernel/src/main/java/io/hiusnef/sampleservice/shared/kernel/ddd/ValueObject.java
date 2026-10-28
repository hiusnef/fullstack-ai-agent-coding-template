package io.hiusnef.sampleservice.shared.kernel.ddd;

/**
 * Marker for value objects: immutable, compared by value, no identity. Implement it on records; invariants are checked
 * in the compact constructor.
 */
public interface ValueObject {}
