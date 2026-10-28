package io.hiusnef.sampleservice;

import static org.assertj.core.api.Assertions.assertThat;

import io.hiusnef.sampleservice.greeting.application.port.in.GreetUseCase;
import io.hiusnef.sampleservice.greeting.domain.GuestGreeted;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@SpringBootTest
@RecordApplicationEvents
class ApplicationContextIT {

    @Autowired
    private GreetUseCase greetUseCase;

    @Autowired
    private ApplicationEvents events;

    @Test
    void shouldWireUseCaseRepositoryAndEvents_whenContextStarts() {
        // when
        String message = greetUseCase.greet("Ada").message();

        // then
        assertThat(message).isEqualTo("Hello, Ada!");
        assertThat(events.stream(GuestGreeted.class))
                .singleElement()
                .satisfies(event -> assertThat(event.name().value()).isEqualTo("Ada"));
    }
}
