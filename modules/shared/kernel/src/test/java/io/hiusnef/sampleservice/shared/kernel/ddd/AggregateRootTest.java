package io.hiusnef.sampleservice.shared.kernel.ddd;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class AggregateRootTest {

    record Happened(Instant occurredOn) implements DomainEvent {}

    static final class Sample extends AggregateRoot<String> {
        Sample(String id) {
            super(id);
        }

        void act() {
            registerEvent(new Happened(Instant.EPOCH));
        }
    }

    @Test
    void shouldReturnAndClearEvents_whenPulled() {
        // given
        Sample sample = new Sample("a");
        sample.act();

        // when / then
        assertThat(sample.pullDomainEvents()).containsExactly(new Happened(Instant.EPOCH));
        assertThat(sample.pullDomainEvents()).isEmpty();
    }

    @Test
    void shouldBeEqual_whenSameTypeAndId() {
        Sample first = new Sample("a");
        first.act();

        assertThat(first).isEqualTo(new Sample("a")).isNotEqualTo(new Sample("b"));
    }
}
