package de.tudl.playground.datorum.modulith.user.query.queryhandler;

import de.tudl.playground.datorum.gateway.query.QueryHandler;
import de.tudl.playground.datorum.modulith.user.command.data.User;
import de.tudl.playground.datorum.modulith.user.command.data.UserRepository;
import de.tudl.playground.datorum.modulith.user.query.queries.GetUserByIdQuery;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * The {@code GetUserByIdQueryHandler} class handles the {@link GetUserByIdQuery} to retrieve a user by their unique ID.
 * <p>
 * This class is responsible for processing the query and interacting with the {@link UserRepository} to fetch the user data
 * from the database. If no user is found for the provided ID, it throws a {@link RuntimeException}.
 * </p>
 */
@Component
public class GetUserByIdQueryHandler implements QueryHandler<GetUserByIdQuery, User> {

    /**
     * The {@link UserRepository} used to retrieve user data from the database.
     */
    private final UserRepository userRepository;

    /**
     * Constructs a {@code GetUserByIdQueryHandler} with the specified {@link UserRepository}.
     *
     * @param userRepository the repository for user data.
     */
    public GetUserByIdQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Handles the {@link GetUserByIdQuery} to fetch the user associated with the provided user ID.
     *
     * @param query the query containing the user ID.
     * @return the {@link User} object corresponding to the user ID.
     * @throws RuntimeException if no user is found for the provided ID.
     */
    @Override
    public User handle(GetUserByIdQuery query) {
        return userRepository.findById(UUID.fromString(query.userId()))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
