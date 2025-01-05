package de.tudl.playground.datorum.modulith.user.command.aggregate;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.tudl.playground.datorum.modulith.eventstore.EventStore;
import de.tudl.playground.datorum.modulith.user.command.data.dto.CreateUserDto;
import de.tudl.playground.datorum.modulith.user.command.data.dto.UpdateUserDto;
import de.tudl.playground.datorum.modulith.user.command.events.UserCreatedEvent;
import de.tudl.playground.datorum.modulith.user.command.events.UserUpdatedEvent;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * The {@code UserAggregate} class serves as the aggregate root for the user domain.
 * It encapsulates all the state and behaviors associated with a user entity,
 * such as creation, updates, and event application.
 *
 * <p>This class is designed to support event sourcing by maintaining a list of domain
 * events that can be used to reconstruct the aggregate's state. It uses the
 * {@link EventStore} for handling persisted events.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Handling user creation and updates via DTOs.</li>
 *     <li>Raising domain events like {@code UserCreatedEvent} and {@code UserUpdatedEvent}.</li>
 *     <li>Applying events to update the aggregate's state.</li>
 *     <li>Rehydrating state from a list of historical events.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * UserAggregate user = new UserAggregate();
 * user.createUser(new CreateUserDto(...));
 * user.updateUser(new UpdateUserDto(...));
 * List<Object> historicalEvents = loadEventsFromStore();
 * user.rehydrate(historicalEvents);
 * }</pre>
 *
 * <p>Note: This class assumes that events are persisted in an {@code EventStore}
 * and provides mechanisms for deserialization and state reconstruction.</p>
 *
 * @see de.tudl.playground.datorum.modulith.user.command.events.UserCreatedEvent
 * @see de.tudl.playground.datorum.modulith.user.command.events.UserUpdatedEvent
 * @see de.tudl.playground.datorum.modulith.eventstore.EventStore
 */

public class UserAggregate {

    private String userId;

    @Getter
    private String userName;

    @Getter
    private String passwordHash;

    @Getter
    private String passwordSalt;

    @Getter
    private String role;

    @Getter
    private List<Object> changes = new ArrayList<>();

    /**
     * Creates a new user in the aggregate using the provided {@link CreateUserDto}.
     * This method generates a {@code UserCreatedEvent} to reflect the changes.
     *
     * @param createDto the data transfer object containing user creation details.
     * @throws IllegalArgumentException if the user already exists in the aggregate.
     */
    public void createUser(CreateUserDto createDto) {
        if (this.userId != null) {
            throw new IllegalArgumentException("User already exists!");
        }

        apply(
                new UserCreatedEvent(
                        createDto.userId(),
                        createDto.userName(),
                        createDto.passwordHash(),
                        createDto.passwordSalt(),
                        createDto.role()
                )
        );
    }

    /**
     * Updates the user information using the provided data transfer object (DTO).
     * This operation raises a {@code UserUpdatedEvent}.
     *
     * @param updateDto the DTO containing user update details.
     * @throws IllegalArgumentException if the user does not exist.
     */
    public void updateUser(UpdateUserDto updateDto) {
        if (this.userId == null) {
            throw new IllegalArgumentException("User does not exist!");
        }

        apply(
                new UserUpdatedEvent(
                        this.userId,
                        updateDto.userName(),
                        updateDto.passwordHash(),
                        updateDto.passwordSalt(),
                        updateDto.role()
                )
        );
    }

    /**
     * Applies a domain event to the aggregate by adding it to the list of changes
     * and updating the aggregate state accordingly.
     *
     * @param event the domain event to apply.
     */
    private void apply(Object event) {
        this.changes.add(event);
        applyEvent(event);
    }

    /**
     * Updates the aggregate state based on the given domain event.
     *
     * @param event the domain event to apply to the aggregate state.
     */
    private void applyEvent(Object event) {
        if (event instanceof UserCreatedEvent userCreatedEvent) {
            this.userId = userCreatedEvent.userId();
            this.userName = userCreatedEvent.username();
            this.passwordHash = userCreatedEvent.passwordHash();
            this.passwordSalt = userCreatedEvent.passwordSalt();
            this.role = userCreatedEvent.role();
        } else if (event instanceof UserUpdatedEvent userUpdatedEvent) {
            this.userName = userUpdatedEvent.userName();
            this.passwordHash = userUpdatedEvent.passwordHash();
            this.passwordSalt = userUpdatedEvent.passwordSalt();
            this.role = userUpdatedEvent.role();
        }
    }

    /**
     * Rebuilds the aggregate's state by applying a list of historical events.
     * Each event is deserialized from its {@link EventStore} representation and
     * applied to update the current state.
     *
     * @param events the list of historical events to process and apply.
     */

    public void rehydrate(List<Object> events) {
        // Flatten the events list (extract EventStore from nested structures)
        events
                .stream()
                .filter(event -> event instanceof List<?>) // Only process if event is a List
                .map(event -> (List<?>) event) // Cast to List
                .flatMap(List::stream) // Flatten nested List
                .filter(EventStore.class::isInstance) // Only process if event is EventStore
                .map(EventStore.class::cast) // Cast to EventStore
                .forEach(this::processEvent); // Process each EventStore
    }

    private void processEvent(EventStore eventStore) {
        // Deserialize and apply the event
        try {
            String eventType = eventStore.getEventType();
            String eventData = eventStore.getEventData();

            Object deserializedEvent = deserializeEvent(eventType, eventData);

            if (deserializedEvent != null) {
                applyEvent(deserializedEvent);
            } else {
                throw new IllegalArgumentException("Unknown event type: " + eventType);
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error processing event: " + eventStore.getEventType(),
                    e
            );
        }
    }

    private Object deserializeEvent(String eventType, String eventData)
            throws Exception {
        return switch (eventType) {
            case "UserCreatedEvent" -> new ObjectMapper()
                    .readValue(eventData, UserCreatedEvent.class);
            case "UserUpdatedEvent" -> new ObjectMapper()
                    .readValue(eventData, UserUpdatedEvent.class);
            // Add other event types as needed
            default -> null;
        };
    }
}
