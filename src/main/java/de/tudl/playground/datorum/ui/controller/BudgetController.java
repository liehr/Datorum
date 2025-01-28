package de.tudl.playground.datorum.ui.controller;

import de.tudl.playground.datorum.gateway.command.CommandGateway;
import de.tudl.playground.datorum.modulith.budget.command.commands.CreateBudgetCommand;
import de.tudl.playground.datorum.modulith.shared.token.AuthTokenProvider;
import de.tudl.playground.datorum.modulith.shared.token.data.Token;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BudgetController {

    private final CommandGateway commandGateway;
    private final AuthTokenProvider authTokenProvider;

    public BudgetController(CommandGateway commandGateway, AuthTokenProvider authTokenProvider) {
        this.commandGateway = commandGateway;
        this.authTokenProvider = authTokenProvider;
    }

    public boolean handleCreateBudget(String name, String description, String amount) {
        if (name == null || name.isEmpty() || description == null || description.isEmpty()) {
            return false;
        }

        if (amount == null || amount.isEmpty()) {
            return false;
        }

        if (Double.parseDouble(amount) < 0) {
            return false;
        }

        Token token = authTokenProvider.getToken();

        CreateBudgetCommand createBudgetCommand = new CreateBudgetCommand(
                UUID.randomUUID().toString(),
                token.userId().toString(),
                name,
                description,
                Double.parseDouble(amount)
        );

        commandGateway.send(createBudgetCommand);

        return true;
    }
}
