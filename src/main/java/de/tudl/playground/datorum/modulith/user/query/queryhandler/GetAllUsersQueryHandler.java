package de.tudl.playground.datorum.modulith.user.query.queryhandler;

import de.tudl.playground.datorum.gateway.query.QueryHandler;
import de.tudl.playground.datorum.modulith.user.command.data.User;
import de.tudl.playground.datorum.modulith.user.command.data.UserRepository;
import de.tudl.playground.datorum.modulith.user.query.queries.GetAllUsersQuery;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The {@code GetAllUsersQueryHandler} class handles the {@link GetAllUsersQuery} to retrieve all users in the system.
 * <p>
 * This class is responsible for processing the query and interacting with the {@link UserRepository} to fetch all user data
 * from the database. The results are returned as a list of {@link User} objects.
 * </p>
 */
@Component
public class GetAllUsersQueryHandler implements QueryHandler<GetAllUsersQuery, List<User>> {

    /**
     * The {@link UserRepository} used to retrieve user data from the database.
     */
    private final UserRepository userRepository;

    /**
     * Constructs a {@code GetAllUsersQueryHandler} with the specified {@link UserRepository}.
     *
     * @param userRepository the repository for user data.
     */
    public GetAllUsersQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Handles the {@link GetAllUsersQuery} to fetch all users from the system.
     *
     * @param query the query requesting all users.
     * @return a list of {@link User} objects representing all users in the system.
     */
    @Override
    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }
}
