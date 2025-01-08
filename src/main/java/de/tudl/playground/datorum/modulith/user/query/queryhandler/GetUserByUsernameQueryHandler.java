package de.tudl.playground.datorum.modulith.user.query.queryhandler;

import de.tudl.playground.datorum.gateway.query.QueryHandler;
import de.tudl.playground.datorum.modulith.user.command.data.User;
import de.tudl.playground.datorum.modulith.user.command.data.UserRepository;
import de.tudl.playground.datorum.modulith.user.query.queries.GetUserByUsername;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetUserByUsernameQueryHandler
        implements QueryHandler<GetUserByUsername, User> {

    private final UserRepository userRepository;

    public GetUserByUsernameQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByUsername query) {
        return userRepository.findUserByUsername(query.username());
    }

}
