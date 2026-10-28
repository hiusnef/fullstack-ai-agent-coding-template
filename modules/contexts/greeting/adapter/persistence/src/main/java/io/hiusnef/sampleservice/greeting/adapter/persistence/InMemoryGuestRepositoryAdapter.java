package io.hiusnef.sampleservice.greeting.adapter.persistence;

import io.hiusnef.sampleservice.greeting.domain.Guest;
import io.hiusnef.sampleservice.greeting.domain.GuestRepository;
import io.hiusnef.sampleservice.greeting.domain.PersonName;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

/** In-memory guest store seeded with a few guests, until a real database is added. */
@Component
class InMemoryGuestRepositoryAdapter implements GuestRepository {

    private final Map<String, Guest> guestsByName = new ConcurrentHashMap<>();

    InMemoryGuestRepositoryAdapter() {
        Stream.of("Ada", "Alan", "Grace", "Linus")
                .map(PersonName::new)
                .map(Guest::register)
                .forEach(this::save);
    }

    @Override
    public Optional<Guest> findByName(PersonName name) {
        return Optional.ofNullable(guestsByName.get(name.normalized()));
    }

    @Override
    public void save(Guest guest) {
        guestsByName.put(guest.name().normalized(), guest);
    }
}
