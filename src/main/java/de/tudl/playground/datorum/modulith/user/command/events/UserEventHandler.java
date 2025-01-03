package de.tudl.playground.datorum.modulith.user.command.events;

import de.tudl.playground.datorum.modulith.user.command.data.User;
import de.tudl.playground.datorum.modulith.user.command.data.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * The {@code UserEventHandler} class listens to user-related domain events such as user creation and updates.
 * It is responsible for persisting user data to the {@link UserRepository} when these events occur.
 *
 * <p>This service acts as an event listener for events like {@link UserCreatedEvent} and {@link UserUpdatedEvent}.
 * Upon receiving such events, it creates or updates {@link User} entities and persists them in the repository.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Listen to {@link UserCreatedEvent} and {@link UserUpdatedEvent} events.</li>
 *     <li>Create or update {@link User} entities based on the event data.</li>
 *     <li>Persist {@link User} entities to the database using {@link UserRepository}.</li>
 * </ul>
 *
 * <p>Note: This class operates within the context of a domain-driven design (DDD) architecture, where events are
 * used to trigger side effects like persisting data in the database.</p>
 *
 * @see UserCreatedEvent
 * @see UserUpdatedEvent
 * @see User
 * @see UserRepository
 */
@Service
public class UserEventHandler {

    private final UserRepository userRepository;

    /**
     * Constructs a {@code UserEventHandler} with the specified {@link UserRepository}.
     *
     * @param userRepository the repository used to persist user data in the database.
     */
    public UserEventHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Handles {@link UserCreatedEvent} by creating a new {@link User} entity and saving it to the {@link UserRepository}.
     *
     * <p>This method listens for user creation events, extracts the user data from the event, creates a new user entity,
     * and persists it to the database.</p>
     *
     * @param event the {@link UserCreatedEvent} that contains the details of the newly created user.
     */
    @EventListener
    public void on(UserCreatedEvent event)
    {
        User user = new User();
        user.setId(UUID.fromString(event.userId()));
        user.setUsername(event.getUsername());
        user.setPasswordHash(event.getPasswordHash());
        user.setPasswordSalt(event.getPasswordSalt());
        user.setRole(event.getRole());

        userRepository.save(user);
    }

    /**
     * Handles {@link UserUpdatedEvent} by updating the details of an existing {@link User} entity and saving the updated entity.
     *
     * <p>This method listens for user update events, retrieves the existing user from the repository, updates its data
     * based on the event, and persists the updated user.</p>
     *
     * @param event the {@link UserUpdatedEvent} that contains the updated details of the user.
     */
    @EventListener
    public void on(UserUpdatedEvent event)
    {
        User user = userRepository.findById(UUID.fromString(event.userId()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setUsername(event.userName());
        user.setPasswordHash(event.getPasswordHash());
        user.setPasswordSalt(event.getPasswordSalt());
        user.setRole(event.getRole());

        userRepository.save(user);
    }
}
