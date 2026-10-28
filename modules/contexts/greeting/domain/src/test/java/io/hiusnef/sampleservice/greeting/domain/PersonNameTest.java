package io.hiusnef.sampleservice.greeting.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PersonNameTest {

    @Test
    void shouldNormalizeToLowerCase_whenValid() {
        assertThat(new PersonName("Ada").normalized()).isEqualTo("ada");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "Ada1", "Ada Lovelace", "Ádá"})
    void shouldReject_whenNotOneToFiftyLatinLetters(String value) {
        assertThatThrownBy(() -> new PersonName(value)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldReject_whenLongerThanFiftyLetters() {
        assertThatThrownBy(() -> new PersonName("a".repeat(51))).isInstanceOf(IllegalArgumentException.class);
    }
}
