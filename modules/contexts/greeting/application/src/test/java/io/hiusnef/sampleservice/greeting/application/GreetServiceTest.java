package io.hiusnef.sampleservice.greeting.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import io.hiusnef.sampleservice.greeting.domain.Guest;
import io.hiusnef.sampleservice.greeting.domain.GuestGreeted;
import io.hiusnef.sampleservice.greeting.domain.GuestRepository;
import io.hiusnef.sampleservice.greeting.domain.PersonName;
import io.hiusnef.sampleservice.shared.kernel.ddd.DomainEventPublisher;
import io.hiusnef.sampleservice.shared.kernel.error.NotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GreetServiceTest {

    private static final Instant NOW = Instant.parse("2026-10-28T08:00:00Z");

    @Mock
    private GuestRepository guests;

    @Mock
    private DomainEventPublisher events;

    private GreetService service;

    @BeforeEach
    void setUp() {
        service = new GreetService(guests, events, Clock.fixed(NOW, ZoneOffset.UTC));
    }

    @Test
    void shouldGreetSaveAndPublish_whenGuestIsKnown() {
        // given
        Guest ada = Guest.register(new PersonName("Ada"));
        given(guests.findByName(new PersonName("Ada"))).willReturn(Optional.of(ada));

        // when
        String message = service.greet("Ada").message();

        // then
        assertThat(message).isEqualTo("Hello, Ada!");
        then(guests).should().save(ada);
        then(events).should().publish(List.of(new GuestGreeted(ada.id(), new PersonName("Ada"), 1, NOW)));
    }

    @Test
    void shouldThrowNotFoundAndChangeNothing_whenGuestIsUnknown() {
        // given
        given(guests.findByName(new PersonName("Nobody"))).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> service.greet("Nobody"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Nobody");
        then(guests).should(never()).save(any());
        then(events).should(never()).publish(any());
    }
}
