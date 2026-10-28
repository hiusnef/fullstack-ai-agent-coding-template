package io.hiusnef.sampleservice.greeting.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class GuestTest {

    private static final Instant NOW = Instant.parse("2026-10-28T08:00:00Z");

    @Test
    void shouldGreetByName_whenGreeted() {
        Guest guest = Guest.register(new PersonName("Ada"));

        assertThat(guest.greet(NOW).message()).isEqualTo("Hello, Ada!");
    }

    @Test
    void shouldCountAndRecordEvent_whenGreeted() {
        // given
        Guest guest = Guest.reconstitute(GuestId.newId(), new PersonName("Ada"), 2);

        // when
        guest.greet(NOW);

        // then
        assertThat(guest.timesGreeted()).isEqualTo(3);
        assertThat(guest.pullDomainEvents())
                .containsExactly(new GuestGreeted(guest.id(), new PersonName("Ada"), 3, NOW));
    }
}
