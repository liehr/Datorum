package de.tudl.playground.datorum.ui.controller;

import de.tudl.playground.datorum.gateway.command.CommandGateway;
import de.tudl.playground.datorum.modulith.auth.command.commands.LogoutUserCommand;
import de.tudl.playground.datorum.modulith.auth.command.events.LogoutEvent;
import de.tudl.playground.datorum.modulith.budget.command.commands.CreateBudgetCommand;
import de.tudl.playground.datorum.modulith.shared.token.AuthTokenProvider;
import de.tudl.playground.datorum.modulith.shared.token.data.Token;
import de.tudl.playground.datorum.ui.util.StageSwitcher;
import de.tudl.playground.datorum.ui.view.login.LoginView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class MainController
{
    private final StageSwitcher stageSwitcher;
    private final CommandGateway commandGateway;
    private final AuthTokenProvider authTokenProvider;
    private Token token;
    public MainController(AuthTokenProvider authTokenProvider, StageSwitcher stageSwitcher, CommandGateway commandGateway) {
        this.stageSwitcher = stageSwitcher;
        this.commandGateway = commandGateway;
        this.authTokenProvider = authTokenProvider;
    }

    public void handleLogout()
    {
        token = authTokenProvider.getToken();
        commandGateway.send(new LogoutUserCommand(token.username()));
    }

    public void handleBudgetCreation()
    {
        token = authTokenProvider.getToken();

        CreateBudgetCommand createCommand = new CreateBudgetCommand(
                UUID.randomUUID().toString(),
                token.userId().toString(),
                "Test",
                "Test",
                100.0
        );

        commandGateway.send(createCommand);
    }

    @EventListener
    private void on (LogoutEvent event)
    {
        log.info("Logout successful for user {} !", event.username());
        stageSwitcher.switchTo(LoginView.class);
    }
}
