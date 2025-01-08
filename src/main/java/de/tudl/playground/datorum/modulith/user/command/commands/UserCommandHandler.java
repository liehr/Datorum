package de.tudl.playground.datorum.modulith.user.command.commands;

import de.tudl.playground.datorum.modulith.eventstore.EventPublisher;
import de.tudl.playground.datorum.modulith.eventstore.EventStore;
import de.tudl.playground.datorum.modulith.eventstore.EventStoreRepository;
import de.tudl.playground.datorum.modulith.eventstore.service.EventProcessorService;
import de.tudl.playground.datorum.modulith.user.command.aggregate.UserAggregate;
import de.tudl.playground.datorum.modulith.user.command.data.dto.CreateUserDto;
import de.tudl.playground.datorum.modulith.user.command.data.dto.UpdateUserDto;
import java.util.Collections;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * The {@code UserCommandHandler} class is a command handler in the CQRS (Command Query Responsibility Segregation) pattern
 * that processes user-related commands in the application.
 * It is responsible for coordinating the interactions between the command model (aggregate root),
 * the event store for storing/retrieving events, and the event publisher for broadcasting domain events.
 *
 * <p>This class handles commands such as user creation and updates by applying domain logic
 * through the {@link UserAggregate}, ensuring consistency and propagating state changes as events.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Retrieve historical events from the event store to rehydrate aggregates.</li>
 *     <li>Execute business logic on aggregates based on the incoming commands.</li>
 *     <li>Publish domain events to notify other parts of the system of state changes.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * // Assuming commands are dispatched via Spring's event mechanism
 * CreateUserCommand createUserCommand = new CreateUserCommand(...);
 * applicationContext.publishEvent(createUserCommand);
 * }</pre>
 *
 * <p>Note: This handler assumes the use of an {@link EventPublisher} for publishing events
 * and an {@link EventStoreRepository} for retrieving and storing events.</p>
 *
 * @see UserAggregate
 * @see EventPublisher
 * @see EventStoreRepository
 */

@Service
public class UserCommandHandler {

    private final EventStoreRepository eventStoreRepository;

    private final EventPublisher eventPublisher;

    private final EventProcessorService eventProcessorService;

    /**
     * Constructs a {@code UserCommandHandler} with the specified event store repository and event publisher.
     *
     * @param eventStoreRepository the repository used to retrieve and store events.
     * @param eventPublisher       the publisher used to broadcast events to subscribers.
     */
    public UserCommandHandler(
            EventStoreRepository eventStoreRepository,
            EventPublisher eventPublisher, EventProcessorService eventProcessorService
    ) {
        this.eventStoreRepository = eventStoreRepository;
        this.eventPublisher = eventPublisher;
        this.eventProcessorService = eventProcessorService;
    }

    /**
     * Handles the {@link CreateUserCommand} by creating a new user.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Creates a new instance of {@link UserAggregate}.</li>
     *     <li>Applies the business logic for creating a user by converting the command into a {@link CreateUserDto}.</li>
     *     <li>Publishes domain events (e.g., {@code UserCreatedEvent}) representing the changes.</li>
     * </ul>
     * </p>
     *
     * @param command the command containing the details required to create a new user.
     */

    @EventListener
    public void handle(CreateUserCommand command) {
        // Create a new user aggregate and rehydrate its state using the retrieved events.
        UserAggregate aggregate = new UserAggregate(eventProcessorService);

        // Convert the command into a DTO to apply the business logic.
        CreateUserDto createDto = new CreateUserDto(
                command.getUserId(),
                command.getUsername(),
                command.getPasswordHash(),
                command.getPasswordSalt(),
                command.getRole()
        );

        // Execute the command logic on the aggregate to create a new user.
        aggregate.createUser(createDto);

        // Publish the changes as domain events to propagate the state changes.
        for (Object event : aggregate.getChanges()) {
            eventPublisher.publishEvent(event);
        }
    }

    /**
     * Handles the {@link UpdateUserCommand} by updating the details of an existing user.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Retrieves historical events for the aggregate from the {@link EventStoreRepository}.</li>
     *     <li>Rehydrates the {@link UserAggregate} to its current state using the retrieved events.</li>
     *     <li>Applies the update logic using a {@link UpdateUserDto} created from the command.</li>
     *     <li>Publishes domain events (e.g., {@code UserUpdatedEvent}) to notify about the changes.</li>
     * </ul>
     * </p>
     *
     * @param command the command containing the user update details.
     */
    @EventListener
    public void handle(UpdateUserCommand command) {
        List<EventStore> events = eventStoreRepository.findByAggregateId(command.getUserId());

        // Create a new user aggregate and rehydrate its state using the retrieved events.
        UserAggregate aggregate = new UserAggregate(eventProcessorService);

        aggregate.rehydrate(events);

        UpdateUserDto updateDto = new UpdateUserDto(
                command.getUsername(),
                command.getPasswordHash(),
                command.getPasswordSalt(),
                command.getRole()
        );

        aggregate.updateUser(updateDto);

        for (Object event : aggregate.getChanges()) {
            eventPublisher.publishEvent(event);
        }
    }
}
