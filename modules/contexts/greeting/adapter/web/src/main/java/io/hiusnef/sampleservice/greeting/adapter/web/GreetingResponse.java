package io.hiusnef.sampleservice.greeting.adapter.web;

import io.hiusnef.sampleservice.greeting.domain.Greeting;

public record GreetingResponse(String message) {

    static GreetingResponse from(Greeting greeting) {
        return new GreetingResponse(greeting.message());
    }
}
