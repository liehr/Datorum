package de.tudl.playground.datorum.modulith.auth.command.aggregate;

import de.tudl.playground.datorum.modulith.auth.command.data.dto.LoginUserDto;
import de.tudl.playground.datorum.modulith.auth.command.events.LoginFailedEvent;
import de.tudl.playground.datorum.modulith.auth.command.events.LoginSuccessfulEvent;
import de.tudl.playground.datorum.modulith.eventstore.EventStore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;


import de.tudl.playground.datorum.modulith.eventstore.service.EventProcessorService;

/**
 * The {@code AuthAggregate} class represents the aggregate root for the authentication domain.
 * It handles login attempts, raises domain events, and supports event sourcing through state
 * rehydration and event application.
 *
 * <p>This class uses the {@link EventProcessorService} to process and apply domain events
 * dynamically, making it extensible and decoupled from specific event deserialization logic.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Handling login attempts and generating corresponding events.</li>
 *     <li>Applying events to update the aggregate's state.</li>
 *     <li>Rehydrating state from historical events stored in an {@link EventStore}.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * AuthAggregate auth = new AuthAggregate(eventProcessorService);
 * auth.handleLoginAttempt(new LoginUserDto("username", true));
 * auth.rehydrate(loadEventsFromStore());
 * }</pre>
 *
 * @see de.tudl.playground.datorum.modulith.auth.command.events.LoginSuccessfulEvent
 * @see de.tudl.playground.datorum.modulith.auth.command.events.LoginFailedEvent
 * @see de.tudl.playground.datorum.modulith.eventstore.EventStore
 */
@Getter
public class AuthAggregate {

    private final EventProcessorService eventProcessorService;

    private String aggregateId;
    private String lastLoginAttemptUsername;
    private boolean lastLoginSuccess;

    private final List<Object> changes = new ArrayList<>();

    public AuthAggregate(EventProcessorService eventProcessorService) {
        this.eventProcessorService = eventProcessorService;
    }

    /**
     * Handles a login attempt and raises the appropriate domain event based on its outcome.
     *
     * @param loginDto the login details, including username and success status.
     */
    public void handleLoginAttempt(LoginUserDto loginDto) {
        if (loginDto.success()) {
            apply(new LoginSuccessfulEvent(loginDto.username(), LocalDateTime.now().toString()));
        } else {
            apply(new LoginFailedEvent(
                    loginDto.username().isBlank() ? "empty_username" : loginDto.username(),
                    LocalDateTime.now().toString()
            ));
        }
    }

    /**
     * Applies an event to the aggregate's state and records it in the list of changes.
     *
     * @param event the domain event to be applied.
     */
    private void apply(Object event) {
        this.changes.add(event);
        applyEvent(event);
    }

    /**
     * Updates the aggregate's state based on the provided event.
     *
     * @param event the event to apply.
     */
    private void applyEvent(Object event) {
        if (event instanceof LoginSuccessfulEvent loginSuccess) {
            this.aggregateId = loginSuccess.username();
            this.lastLoginAttemptUsername = loginSuccess.username();
            this.lastLoginSuccess = true;
        } else if (event instanceof LoginFailedEvent loginFailed) {
            this.aggregateId = loginFailed.username();
            this.lastLoginAttemptUsername = loginFailed.username();
            this.lastLoginSuccess = false;
        }
    }

    /**
     * Rehydrates the aggregate's state from a list of historical events.
     *
     * @param events the list of events to reapply.
     */
    public void rehydrate(List<EventStore> events) {
        events.forEach(eventStore -> {
            eventProcessorService.processEvent(eventStore, this::applyEvent);
        });
    }
}