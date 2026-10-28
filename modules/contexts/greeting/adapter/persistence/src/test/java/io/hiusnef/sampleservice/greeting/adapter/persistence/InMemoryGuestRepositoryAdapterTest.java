package io.hiusnef.sampleservice.greeting.adapter.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.hiusnef.sampleservice.greeting.domain.Guest;
import io.hiusnef.sampleservice.greeting.domain.PersonName;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class InMemoryGuestRepositoryAdapterTest {

    private final InMemoryGuestRepositoryAdapter repository = new InMemoryGuestRepositoryAdapter();

    @Test
    void shouldFindSeededGuest_whenNameMatchesIgnoringCase() {
        assertThat(repository.findByName(new PersonName("aDA")))
                .map(guest -> guest.name().value())
                .contains("Ada");
    }

    @Test
    void shouldFindNothing_whenGuestIsUnknown() {
        assertThat(repository.findByName(new PersonName("Nobody"))).isEmpty();
    }

    @Test
    void shouldKeepState_whenSaved() {
        // given
        Guest ada = repository.findByName(new PersonName("Ada")).orElseThrow();
        ada.greet(Instant.EPOCH);

        // when
        repository.save(ada);

        // then
        assertThat(repository.findByName(new PersonName("Ada")))
                .map(Guest::timesGreeted)
                .contains(1);
    }
}
