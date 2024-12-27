package de.tudl.playground.datorum.modulith.user.command.aggregate;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.tudl.playground.datorum.modulith.user.command.data.dto.CreateUserDto;
import de.tudl.playground.datorum.modulith.user.command.data.dto.UpdateUserDto;
import de.tudl.playground.datorum.modulith.user.command.events.UserCreatedEvent;
import de.tudl.playground.datorum.modulith.user.command.events.UserUpdatedEvent;
import de.tudl.playground.datorum.modulith.eventstore.EventStore;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code UserAggregate} class represents the aggregate root for the user domain.
 * It encapsulates the state and behavior for managing users, including creation, updates,
 * and the application of domain events. The class supports event sourcing by storing
 * changes as a list of domain events.
 */
public class UserAggregate {

    /**
     * Unique identifier for the user.
     */
    private String userId;

    /**
     * The username of the user.
     */
    @Getter
    private String userName;

    /**
     * The hashed password of the user.
     */
    @Getter
    private String passwordHash;

    /**
     * The salt used in the hashing of the user's password.
     */
    @Getter
    private String passwordSalt;

    /**
     * The role assigned to the user.
     */
    @Getter
    private String role;

    /**
     * List of changes (domain events) applied to the aggregate.
     * These events can be used to rebuild the aggregate state for event sourcing.
     */
    @Getter
    private List<Object> changes = new ArrayList<>();

    /**
     * Creates a new user using the provided data transfer object (DTO).
     * This operation raises a {@code UserCreatedEvent}.
     *
     * @param createDto the DTO containing user creation details.
     * @throws IllegalArgumentException if the user already exists.
     */
    public void createUser(CreateUserDto createDto) {
        if (this.userId != null) {
            throw new IllegalArgumentException("User already exists!");
        }

        apply(new UserCreatedEvent(
                createDto.userId(),
                createDto.userName(),
                createDto.passwordHash(),
                createDto.passwordSalt(),
                createDto.role()
        ));
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

        apply(new UserUpdatedEvent(
                this.userId,
                updateDto.userName(),
                updateDto.passwordHash(),
                updateDto.passwordSalt(),
                updateDto.role()
        ));
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
     * Rebuilds the aggregate state by applying a list of historical domain events.
     *
     * @param events the list of historical domain events to apply.
     */
    public void rehydrate(List<Object> events) {
        // Flatten the events list (extract EventStore from nested structures)
        events.stream()
                .filter(event -> event instanceof List<?>)  // Only process if event is a List
                .map(event -> (List<?>) event)  // Cast to List
                .flatMap(List::stream)  // Flatten nested List
                .filter(EventStore.class::isInstance)  // Only process if event is EventStore
                .map(EventStore.class::cast)  // Cast to EventStore
                .forEach(this::processEvent);  // Process each EventStore
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
            throw new RuntimeException("Error processing event: " + eventStore.getEventType(), e);
        }
    }

    private Object deserializeEvent(String eventType, String eventData) throws Exception {
        return switch (eventType) {
            case "UserCreatedEvent" -> new ObjectMapper().readValue(eventData, UserCreatedEvent.class);
            case "UserUpdatedEvent" -> new ObjectMapper().readValue(eventData, UserUpdatedEvent.class);
            // Add other event types as needed
            default -> null;
        };
    }

}
