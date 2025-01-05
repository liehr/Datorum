package de.tudl.playground.datorum.modulith.user.query.queryhandler;

import de.tudl.playground.datorum.gateway.query.QueryHandler;
import de.tudl.playground.datorum.modulith.user.command.data.User;
import de.tudl.playground.datorum.modulith.user.command.data.UserRepository;
import de.tudl.playground.datorum.modulith.user.query.queries.GetAllUsersQuery;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * The {@code GetAllUsersQueryHandler} class is responsible for handling the {@link GetAllUsersQuery}.
 * It fetches all users from the system by interacting with the {@link UserRepository}.
 *
 * <p>This query handler is used to retrieve a complete list of users present in the database. The query handler
 * processes the incoming query and delegates the actual data retrieval to the {@link UserRepository}. The result is
 * a list of {@link User} objects.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Handling the {@link GetAllUsersQuery} query.</li>
 *     <li>Interacting with the {@link UserRepository} to retrieve all user data from the database.</li>
 *     <li>Returning the list of {@link User} entities representing all users in the system.</li>
 * </ul>
 *
 * @see GetAllUsersQuery
 * @see UserRepository
 * @see User
 */
@Component
public class GetAllUsersQueryHandler
        implements QueryHandler<GetAllUsersQuery, List<User>> {

    /**
     * The {@link UserRepository} used to retrieve user data from the database.
     */
    private final UserRepository userRepository;

    /**
     * Constructs a {@code GetAllUsersQueryHandler} with the specified {@link UserRepository}.
     *
     * @param userRepository the repository used to fetch user data from the database.
     */
    public GetAllUsersQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Handles the {@link GetAllUsersQuery} to fetch all users from the system.
     *
     * <p>This method processes the incoming {@link GetAllUsersQuery} and retrieves all users from the system
     * using the {@link UserRepository}. It returns the data as a list of {@link User} objects.</p>
     *
     * @param query the query requesting all users in the system.
     * @return a list of {@link User} objects representing all users in the system.
     */
    @Override
    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }
}
