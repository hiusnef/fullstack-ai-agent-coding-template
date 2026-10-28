package io.hiusnef.sampleservice.greeting.application.port.in;

import io.hiusnef.sampleservice.greeting.domain.Greeting;

/** Input port and public API of the greeting context: what other adapters and contexts may ask it to do. */
public interface GreetUseCase {

    /**
     * @throws io.hiusnef.sampleservice.shared.kernel.error.NotFoundException if no guest has this name
     * @throws IllegalArgumentException if the name is not a valid {@link io.hiusnef.sampleservice.greeting.domain.PersonName}
     */
    Greeting greet(String name);
}
