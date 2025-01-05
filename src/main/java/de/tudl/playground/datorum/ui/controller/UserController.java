package de.tudl.playground.datorum.ui.controller;

import de.tudl.playground.datorum.gateway.command.CommandGateway;
import de.tudl.playground.datorum.gateway.query.QueryGateway;
import de.tudl.playground.datorum.modulith.user.command.commands.CreateUserCommand;
import de.tudl.playground.datorum.modulith.user.command.commands.UpdateUserCommand;
import de.tudl.playground.datorum.modulith.user.command.data.User;
import de.tudl.playground.datorum.modulith.user.query.queries.GetAllUsersQuery;
import de.tudl.playground.datorum.modulith.user.query.queries.GetUserByIdQuery;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    private final QueryGateway queryGateway;
    private final CommandGateway commandGateway;

    public User getUserById(String uuid) {
        GetUserByIdQuery query = new GetUserByIdQuery(uuid);

        Object user = queryGateway.query(query);

        return (User) user;
    }

    public List<User> getAllUsers() {
        GetAllUsersQuery query = new GetAllUsersQuery();

        return (List<User>) queryGateway.query(query);
    }

    public void createUser() {
        CreateUserCommand createUserCommand = new CreateUserCommand(
                UUID.randomUUID().toString(),
                "My awesome Username",
                "MyPassWordHash",
                "MySalt",
                "USER"
        );

        commandGateway.send(createUserCommand);
    }

    public void updateUser(String uuid) {
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(
                uuid,
                "MyTestName",
                "Test",
                "Blablabla",
                "AWESOMECLASS"
        );

        commandGateway.send(updateUserCommand);
    }

    @Autowired
    public UserController(
            QueryGateway queryGateway,
            CommandGateway commandGateway
    ) {
        this.queryGateway = queryGateway;
        this.commandGateway = commandGateway;
    }
}
