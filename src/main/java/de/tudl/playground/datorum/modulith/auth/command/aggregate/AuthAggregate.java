package de.tudl.playground.datorum.modulith.auth.command.aggregate;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.tudl.playground.datorum.modulith.auth.command.data.dto.LoginUserDto;
import de.tudl.playground.datorum.modulith.auth.command.events.LoginFailedEvent;
import de.tudl.playground.datorum.modulith.auth.command.events.LoginSuccessfulEvent;
import de.tudl.playground.datorum.modulith.shared.exception.ErrorProcessingEventException;
import de.tudl.playground.datorum.modulith.eventstore.EventStore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.SneakyThrows;

/**
 * The {@code AuthAggregate} class represents the aggregate root for the authentication domain.
 * It handles login attempts, raises domain events, and supports event sourcing through state
 * rehydration and event application.
 *
 * <p>This class maintains the state of authentication attempts, including the last login
 * username and success status. It records domain events such as {@link LoginSuccessfulEvent}
 * and {@link LoginFailedEvent}, enabling event-driven state management and persistence.</p>
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
 * AuthAggregate auth = new AuthAggregate();
 * auth.handleLoginAttempt(new LoginUserDto("username", true));
 * auth.rehydrate(loadEventsFromStore());
 * }</pre>
 *
 * <h2>Error Handling</h2>
 * <ul>
 *     <li>{@link ErrorProcessingEventException}: Thrown for errors during event processing.</li>
 *     <li>{@code IllegalArgumentException}: Raised for unknown or unsupported event types.</li>
 * </ul>
 *
 * <p>Note: This class assumes that all events conform to expected formats for successful
 * deserialization and state reconstruction.</p>
 *
 * @see de.tudl.playground.datorum.modulith.auth.command.events.LoginSuccessfulEvent
 * @see de.tudl.playground.datorum.modulith.auth.command.events.LoginFailedEvent
 * @see de.tudl.playground.datorum.modulith.eventstore.EventStore
 */
@Getter
public class AuthAggregate {

    private String aggregateId;

    private String lastLoginAttemptUsername;

    private boolean lastLoginSuccess;

    private final List<Object> changes = new ArrayList<>();

    /**
     * Handles a login attempt and raises the appropriate domain event based on its outcome.
     *
     * @param loginDto the login details, including username and success status.
     */
    public void handleLoginAttempt(LoginUserDto loginDto) {
        if (loginDto.success()) apply(
                new LoginSuccessfulEvent(
                        loginDto.username(),
                        LocalDateTime.now().toString()
                )
        );
        else apply(
                new LoginFailedEvent(
                        loginDto.username().isBlank() ? "empty_username" : loginDto.username(),
                        LocalDateTime.now().toString()
                )
        );
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
    public void rehydrate(List<Object> events) {
        events
                .stream()
                .filter(event -> event instanceof List<?>)
                .map(event -> (List<?>) event)
                .flatMap(List::stream)
                .filter(EventStore.class::isInstance)
                .map(EventStore.class::cast)
                .forEach(this::processEvent);
    }

    /**
     * Processes an event from the event store, deserializing and applying it.
     *
     * @param eventStore the event store containing event data.
     */
    @SneakyThrows
    private void processEvent(EventStore eventStore) {
        try {
            String eventType = eventStore.getEventType();
            String eventData = eventStore.getEventData();

            Object deserializedEvent = deserializeEvent(eventType, eventData);

            if (deserializedEvent != null) applyEvent(deserializedEvent);
            else throw new IllegalArgumentException(
                    "Unknown event type: " + eventType
            );
        } catch (Exception e) {
            throw new ErrorProcessingEventException(
                    "Error processing event: " + eventStore.getEventType(),
                    e
            );
        }
    }

    /**
     * Deserializes an event based on its type.
     *
     * @param eventType the type of the event.
     * @param eventData the serialized event data.
     * @return the deserialized event object, or {@code null} if the type is unknown.
     */
    @SneakyThrows
    private Object deserializeEvent(String eventType, String eventData) {
        return switch (eventType) {
            case "LoginSuccessfulEvent" -> new ObjectMapper()
                    .readValue(eventData, LoginSuccessfulEvent.class);
            case "LoginFailedEvent" -> new ObjectMapper()
                    .readValue(eventData, LoginFailedEvent.class);
            default -> null;
        };
    }
}

