package de.tudl.playground.datorum.modulith.auth.command.events;

import de.tudl.playground.datorum.modulith.auth.command.data.User;
import de.tudl.playground.datorum.modulith.auth.command.data.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Event handler for handling {@link UserCreatedEvent} and persisting user data in the repository.
 * <p>
 * This service listens to user creation events and saves the corresponding user data into the database
 * using the {@link UserRepository}.
 * </p>
 */
@Service
public class UserEventHandler {

    private final UserRepository userRepository;

    /**
     * Constructs a {@code UserEventHandler} with the given {@link UserRepository}.
     *
     * @param userRepository the repository used to persist user data.
     */
    public UserEventHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Event listener method that listens to {@link UserCreatedEvent} events.
     * It creates a new {@link User} entity from the event's data and saves it to the {@link UserRepository}.
     *
     * @param event the {@link UserCreatedEvent} containing the data of the created user.
     */
    @EventListener
    public void on(UserCreatedEvent event) {
        User user = new User();
        user.setUsername(event.getUsername());
        user.setPasswordHash(event.getPasswordHash());
        user.setPasswordSalt(event.getPasswordSalt());
        user.setRole(event.getRole());

        userRepository.save(user);
    }
}
