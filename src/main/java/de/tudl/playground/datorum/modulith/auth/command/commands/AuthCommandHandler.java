package de.tudl.playground.datorum.modulith.auth.command.commands;

import de.tudl.playground.datorum.modulith.auth.command.aggregate.AuthAggregate;
import de.tudl.playground.datorum.modulith.auth.command.data.dto.LoginUserDto;
import de.tudl.playground.datorum.modulith.auth.command.events.ValidateCredentialsEvent;
import de.tudl.playground.datorum.modulith.eventstore.EventPublisher;
import de.tudl.playground.datorum.modulith.eventstore.EventStoreRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class AuthCommandHandler {

    private final EventStoreRepository eventStoreRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final EventPublisher eventPublisher;

    public AuthCommandHandler(
            EventStoreRepository eventStoreRepository,
            ApplicationEventPublisher eventPublisher,
            EventPublisher eventPublisher1
    ) {
        this.eventStoreRepository = eventStoreRepository;
        this.applicationEventPublisher = eventPublisher;
        this.eventPublisher = eventPublisher1;
    }

    @EventListener
    public void handle(LoginUserCommand command) {
        boolean success = validateCredentials(
                command.getUsername(),
                command.getPassword()
        );

        AuthAggregate authAggregate = new AuthAggregate();

        LoginUserDto loginUserDto = new LoginUserDto(
                command.getUsername(),
                success
        );

        authAggregate.handleLoginAttempt(loginUserDto);

        for (Object event : authAggregate.getChanges()) {
            eventPublisher.publishEvent(event);
        }
    }

    private boolean validateCredentials(String username, String password) {
        ValidateCredentialsEvent event = new ValidateCredentialsEvent(
                username,
                password,
                false
        );

        // Use ApplicationEventPublisher from Spring Boot so no credentials are persisted.
        applicationEventPublisher.publishEvent(event);
        return event.isSuccess();
    }
}
