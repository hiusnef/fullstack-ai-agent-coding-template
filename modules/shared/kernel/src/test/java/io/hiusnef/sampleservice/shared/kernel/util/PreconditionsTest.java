package io.hiusnef.sampleservice.shared.kernel.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class PreconditionsTest {

    @Test
    void shouldReturnValue_whenNotBlank() {
        assertThat(Preconditions.requireNonBlank("Ada", "name")).isEqualTo("Ada");
    }

    @Test
    void shouldRejectWithName_whenBlank() {
        assertThatThrownBy(() -> Preconditions.requireNonBlank("  ", "name"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("name must not be blank");
    }

    @Test
    void shouldRejectWithName_whenNull() {
        assertThatThrownBy(() -> Preconditions.requireNonBlank(null, "name"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name");
    }
}
