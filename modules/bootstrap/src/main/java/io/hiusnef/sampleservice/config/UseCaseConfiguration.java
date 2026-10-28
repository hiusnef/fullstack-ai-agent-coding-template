package io.hiusnef.sampleservice.config;

import io.hiusnef.sampleservice.greeting.application.GreetService;
import io.hiusnef.sampleservice.greeting.application.port.in.GreetUseCase;
import io.hiusnef.sampleservice.greeting.domain.GuestRepository;
import io.hiusnef.sampleservice.shared.kernel.ddd.DomainEventPublisher;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Registers framework-free use cases as beans; one @Bean per use case, typed as its input port. */
@Configuration(proxyBeanMethods = false)
class UseCaseConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    // --- greeting context
    @Bean
    GreetUseCase greetUseCase(GuestRepository guests, DomainEventPublisher events, Clock clock) {
        return new GreetService(guests, events, clock);
    }
}
