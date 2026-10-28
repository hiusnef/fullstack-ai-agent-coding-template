package io.hiusnef.sampleservice.greeting.adapter.web;

import io.hiusnef.sampleservice.greeting.application.port.in.GreetUseCase;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/greetings")
class GreetingController {

    private final GreetUseCase greetUseCase;

    GreetingController(GreetUseCase greetUseCase) {
        this.greetUseCase = greetUseCase;
    }

    @GetMapping("/{name}")
    GreetingResponse greet(@PathVariable @Pattern(regexp = "[A-Za-z]{1,50}") String name) {
        return GreetingResponse.from(greetUseCase.greet(name));
    }
}
