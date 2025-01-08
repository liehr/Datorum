package de.tudl.playground.datorum.config;

import de.tudl.playground.datorum.modulith.shared.event.EventRegistrationService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventRegistrationConfig {
    @Bean
    public ApplicationRunner eventRegistrar(EventRegistrationService eventRegistrationService)
    {
        return args -> eventRegistrationService.registerAnnotatedEvent("de.tudl.playground.datorum.modulith");
    }
}
