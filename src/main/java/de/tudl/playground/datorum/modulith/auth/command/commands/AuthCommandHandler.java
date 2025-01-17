package de.tudl.playground.datorum.modulith.auth.command.commands;

import de.tudl.playground.datorum.gateway.query.QueryGateway;
import de.tudl.playground.datorum.modulith.auth.command.aggregate.AuthAggregate;
import de.tudl.playground.datorum.modulith.auth.command.data.dto.LoginUserDto;
import de.tudl.playground.datorum.modulith.auth.command.data.dto.LogoutUserDto;
import de.tudl.playground.datorum.modulith.auth.command.events.LoginFailedEvent;
import de.tudl.playground.datorum.modulith.eventstore.EventPublisher;
import de.tudl.playground.datorum.modulith.eventstore.EventStoreRepository;
import de.tudl.playground.datorum.modulith.eventstore.service.EventProcessorService;
import de.tudl.playground.datorum.modulith.shared.util.HashingUtil;
import de.tudl.playground.datorum.modulith.user.command.data.User;
import de.tudl.playground.datorum.modulith.user.query.queries.GetUserByUsername;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * The {@code AuthCommandHandler} class is responsible for handling authentication-related commands
 * in the application. It processes user login attempts, validates credentials, and raises events
 * for further processing in an event-driven architecture.
 *
 * <p>This class listens for login commands, retrieves user data via the {@link QueryGateway},
 * validates user credentials, and updates the authentication state through the
 * {@link AuthAggregate}. It uses the {@link EventPublisher} to publish domain events for
 * downstream consumers and the {@link ApplicationEventPublisher} for failure events.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Handling {@link LoginUserCommand} and orchestrating authentication logic.</li>
 *     <li>Retrieving user data using the {@link QueryGateway}.</li>
 *     <li>Validating user credentials securely.</li>
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
 * <p>Note: This handler is stateless and relies on the {@link QueryGateway} to fetch
 * user data and the {@link HashingUtil} to validate credentials securely. Domain events
 * are dispatched through {@link EventPublisher}, and login failure events are published
 * via {@link ApplicationEventPublisher}.</p>
 *
 * @see AuthAggregate
 * @see LoginUserCommand
 * @see LoginFailedEvent
 */
@Service
@Slf4j
public class AuthCommandHandler {

    private final EventStoreRepository eventStoreRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final EventPublisher eventPublisher;

    private final QueryGateway queryGateway;

    private final EventProcessorService eventProcessorService;

    /**
     * Constructs an {@code AuthCommandHandler} with the required dependencies.
     *
     * @param eventStoreRepository the repository for storing and retrieving events.
     * @param eventPublisher the Spring {@link ApplicationEventPublisher} for handling failure events.
     * @param eventPublisher1 the custom {@link EventPublisher} for publishing domain events.
     * @param queryGateway the {@link QueryGateway} for querying user data.
     */
    public AuthCommandHandler(
            EventStoreRepository eventStoreRepository,
            ApplicationEventPublisher eventPublisher,
            EventPublisher eventPublisher1,
            QueryGateway queryGateway, EventProcessorService eventProcessorService
    ) {
        this.eventStoreRepository = eventStoreRepository;
        this.applicationEventPublisher = eventPublisher;
        this.eventPublisher = eventPublisher1;
        this.queryGateway = queryGateway;
        this.eventProcessorService = eventProcessorService;
    }

    /**
     * Handles the {@link LoginUserCommand} by orchestrating the login process.
     *
     * <p>This method retrieves user data using the {@link QueryGateway} and validates the provided
     * credentials using the {@link HashingUtil}. It updates the authentication state by invoking
     * methods on the {@link AuthAggregate}. Domain events generated during the login process are
     * published using the {@link EventPublisher}, while login failure events are published via
     * the {@link ApplicationEventPublisher}.</p>
     *
     * @param command the login command containing the username and password.
     */
    @EventListener
    public void handle(LoginUserCommand command) {
        Optional<User> optionalUser = fetchUser(command.getUsername());

        if (optionalUser.isEmpty()) {
            handleUserNotFound(command.getUsername());
            return;
        }

        boolean success = validateCredentials(
                command.getPassword(),
                optionalUser.get().getPasswordHash(),
                optionalUser.get().getPasswordSalt()
        );

        processLoginAttempt(command.getUsername(), optionalUser.get().getRole(), success);
    }

    @EventListener
    public void handle(LogoutUserCommand command)
    {
        processLogoutAttempt(command.getUsername());
    }

    private Optional<User> fetchUser(String username) {
        return queryGateway.query(new GetUserByUsername(username));
    }

    private void handleUserNotFound(String username) {
        log.warn("User {} not found during login attempt!", username);
        applicationEventPublisher.publishEvent(
                new LoginFailedEvent(username, LocalDateTime.now().toString())
        );
    }

    private void processLoginAttempt(String username, String role, boolean success) {
        AuthAggregate authAggregate = new AuthAggregate(eventProcessorService);

        LoginUserDto loginUserDto = new LoginUserDto(username, role, success);
        authAggregate.handleLoginAttempt(loginUserDto);

        publishDomainEvents(authAggregate);
    }

    private void processLogoutAttempt(String username)
    {
        AuthAggregate authAggregate = new AuthAggregate(eventProcessorService);

        LogoutUserDto logoutUserDto = new LogoutUserDto(username);
        authAggregate.handleLogoutAttempt(logoutUserDto);

        publishDomainEvents(authAggregate);
    }

    private void publishDomainEvents(AuthAggregate authAggregate) {
        for (Object event : authAggregate.getChanges()) {
            eventPublisher.publishEvent(event);
        }
    }

    /**
     * Validates the provided password against the stored password hash and salt.
     *
     * <p>This method uses the {@link HashingUtil} to verify if the provided password matches
     * the hashed password stored for the user.</p>
     *
     * @param password the password to validate.
     * @param passwordHash the stored hash of the user's password.
     * @param passwordSalt the salt used to hash the user's password.
     * @return {@code true} if the credentials are valid; {@code false} otherwise.
     */
    private boolean validateCredentials(
            String password,
            String passwordHash,
            String passwordSalt
    ) {
        return HashingUtil.verifyPassword(password, passwordHash, passwordSalt);
    }
}