package de.tudl.playground.datorum.modulith.user.command.commands;

import de.tudl.playground.datorum.modulith.user.command.aggregate.UserAggregate;
import de.tudl.playground.datorum.modulith.user.command.data.dto.CreateUserDto;
import de.tudl.playground.datorum.modulith.user.command.data.dto.UpdateUserDto;
import de.tudl.playground.datorum.modulith.eventstore.EventPublisher;
import de.tudl.playground.datorum.modulith.eventstore.EventStoreRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * The {@code UserCommandHandler} class handles user-related commands within the application.
 * It coordinates between the command model (user aggregate), the event store for event retrieval,
 * and the event publisher for broadcasting domain events.
 * <p>
 * This class ensures that user-related commands are processed in a consistent manner,
 * applying business logic and triggering domain events when necessary.
 * </p>
 */
@Service
public class UserCommandHandler {

    /**
     * Repository responsible for accessing and storing events in the event store.
     * Used to retrieve events related to aggregates before applying business logic.
     */
    private final EventStoreRepository eventStoreRepository;

    /**
     * Publisher responsible for broadcasting domain events to the system.
     * Events published here are used to propagate changes in state to other parts of the system.
     */
    private final EventPublisher eventPublisher;

    /**
     * Constructs a {@code UserCommandHandler} with the specified event store repository and event publisher.
     *
     * @param eventStoreRepository the repository used to retrieve and store events.
     * @param eventPublisher       the publisher used to broadcast events to subscribers.
     */
    public UserCommandHandler(EventStoreRepository eventStoreRepository, EventPublisher eventPublisher) {
        this.eventStoreRepository = eventStoreRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Handles the {@link CreateUserCommand}, executing the logic to create a new user.
     * <p>
     * This method retrieves the existing events for the user aggregate from the event store,
     * rehydrates the aggregate, applies the command logic, and publishes domain events that
     * represent changes in the user state.
     * </p>
     *
     * @param command the command containing the user creation details.
     */
    @EventListener
    public void handle(CreateUserCommand command) {
        // Create a new user aggregate and rehydrate its state using the retrieved events.
        UserAggregate aggregate = new UserAggregate();

        // Convert the command into a DTO to apply the business logic.
        CreateUserDto createDto = new CreateUserDto(
                command.getUserId(),
                command.getUsername(),
                command.getPasswordHash(),
                command.getPasswordSalt(),
                command.getRole());

        // Execute the command logic on the aggregate to create a new user.
        aggregate.createUser(createDto);

        // Publish the changes as domain events to propagate the state changes.
        for (Object event : aggregate.getChanges()) {
            eventPublisher.publishEvent(event);
        }
    }

    @EventListener
    public void handle(UpdateUserCommand command)
    {
        List<Object> events = Collections.singletonList(eventStoreRepository.findByAggregateId(command.getUserId()));

        // Create a new user aggregate and rehydrate its state using the retrieved events.
        UserAggregate aggregate = new UserAggregate();

        if (events.size() > 0)
            aggregate.rehydrate(events);

        UpdateUserDto updateDto = new UpdateUserDto(
                command.getUsername(),
                command.getPasswordHash(),
                command.getPasswordSalt(),
                command.getRole()
        );

        aggregate.updateUser(updateDto);

        for (Object event : aggregate.getChanges())
        {
            eventPublisher.publishEvent(event);
        }
    }
}
