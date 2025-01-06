package de.tudl.playground.datorum.modulith.auth.command.commands;

import de.tudl.playground.datorum.modulith.auth.command.aggregate.AuthAggregate;
import de.tudl.playground.datorum.modulith.auth.command.data.dto.LoginUserDto;
import de.tudl.playground.datorum.modulith.auth.command.events.ValidateCredentialsEvent;
import de.tudl.playground.datorum.modulith.eventstore.EventPublisher;
import de.tudl.playground.datorum.modulith.eventstore.EventStoreRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * The {@code AuthCommandHandler} class is responsible for handling authentication-related commands
 * in the application. It processes user login attempts, validates credentials, and raises events
 * for further processing in an event-driven architecture.
 *
 * <p>This class listens to log in commands, validates user credentials using the
 * {@link ApplicationEventPublisher}, and updates the authentication state through the
 * {@link AuthAggregate}. It uses the {@link EventPublisher} to publish domain events for
 * downstream consumers.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Handling {@link LoginUserCommand} and initiating authentication logic.</li>
 *     <li>Validating user credentials securely using {@code ApplicationEventPublisher}.</li>
 *     <li>Updating authentication state via {@link AuthAggregate}.</li>
 *     <li>Publishing domain events generated during the login process.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * // Example: Handling a login command
 * LoginUserCommand command = new LoginUserCommand("username", "password");
 * authCommandHandler.handle(command);
 * }</pre>
 *
 * <p>Note: This handler relies on Spring's {@link ApplicationEventPublisher} for secure,
 * stateless credential validation and {@link EventPublisher} for event dispatching.</p>
 *
 * @see AuthAggregate
 * @see LoginUserCommand
 * @see ValidateCredentialsEvent
 */
@Service
public class AuthCommandHandler {

    private final EventStoreRepository eventStoreRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final EventPublisher eventPublisher;

    /**
     * Constructs an {@code AuthCommandHandler} with the required dependencies.
     *
     * @param eventStoreRepository the repository for storing and retrieving events.
     * @param eventPublisher the Spring {@link ApplicationEventPublisher} for handling events.
     * @param eventPublisher1 the custom {@link EventPublisher} for publishing domain events.
     */
    public AuthCommandHandler(
            EventStoreRepository eventStoreRepository,
            ApplicationEventPublisher eventPublisher,
            EventPublisher eventPublisher1
    ) {
        this.eventStoreRepository = eventStoreRepository;
        this.applicationEventPublisher = eventPublisher;
        this.eventPublisher = eventPublisher1;
    }

    /**
     * Handles the {@link LoginUserCommand} by validating credentials, updating the
     * authentication state via {@link AuthAggregate}, and publishing domain events.
     *
     * @param command the login command containing the username and password.
     */
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

    /**
     * Validates the provided username and password using a stateless event-driven mechanism.
     *
     * @param username the username to validate.
     * @param password the password to validate.
     * @return {@code true} if the credentials are valid; {@code false} otherwise.
     */
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
