package io.hiusnef.sampleservice.greeting.domain;

import java.util.Optional;

/** Collection-like access to {@link Guest} aggregates, in domain terms. Implemented by the persistence adapter. */
public interface GuestRepository {

    Optional<Guest> findByName(PersonName name);

    void save(Guest guest);
}
