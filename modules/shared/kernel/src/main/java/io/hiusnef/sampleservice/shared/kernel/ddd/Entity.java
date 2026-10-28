package io.hiusnef.sampleservice.shared.kernel.ddd;

import java.util.Objects;

/** An object with identity: two entities are equal when they have the same type and id, whatever their state. */
public abstract class Entity<ID> {

    private final ID id;

    protected Entity(ID id) {
        this.id = Objects.requireNonNull(id, "id");
    }

    public ID id() {
        return id;
    }

    @Override
    public final boolean equals(Object other) {
        return this == other || (other != null && getClass() == other.getClass() && id.equals(((Entity<?>) other).id));
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }
}
