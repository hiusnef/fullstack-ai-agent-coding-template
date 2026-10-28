package io.hiusnef.sampleservice.greeting.adapter.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import io.hiusnef.sampleservice.greeting.application.port.in.GreetUseCase;
import io.hiusnef.sampleservice.greeting.domain.Greeting;
import io.hiusnef.sampleservice.greeting.domain.PersonName;
import io.hiusnef.sampleservice.shared.kernel.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(GreetingController.class)
class GreetingControllerTest {

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private GreetUseCase greetUseCase;

    @Test
    void shouldReturnGreeting_whenNameIsValid() {
        // given
        given(greetUseCase.greet("Ada")).willReturn(new Greeting(new PersonName("Ada")));

        // when / then
        assertThat(mvc.get().uri("/v1/greetings/Ada"))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.message")
                .isEqualTo("Hello, Ada!");
    }

    @Test
    void shouldReturnBadRequest_whenNameHasInvalidCharacters() {
        assertThat(mvc.get().uri("/v1/greetings/Ada123"))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .hasContentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON);
    }

    @Test
    void shouldReturnProblemNotFound_whenServiceThrowsNotFound() {
        // given
        given(greetUseCase.greet("Nobody")).willThrow(new NotFoundException("No greeting for name 'Nobody'"));

        // when / then
        assertThat(mvc.get().uri("/v1/greetings/Nobody"))
                .hasStatus(HttpStatus.NOT_FOUND)
                .bodyJson()
                .extractingPath("$.type")
                .isEqualTo("https://hiusnef.io/problems/not-found");
    }
}
