package de.tudl.playground.datorum.ui.controller;

import de.tudl.playground.datorum.gateway.command.CommandGateway;
import de.tudl.playground.datorum.gateway.query.QueryGateway;
import de.tudl.playground.datorum.modulith.auth.command.commands.LogoutUserCommand;
import de.tudl.playground.datorum.modulith.auth.command.events.LogoutEvent;
import de.tudl.playground.datorum.modulith.budget.command.data.Budget;
import de.tudl.playground.datorum.modulith.budget.query.queries.GetAllBudgetsQuery;
import de.tudl.playground.datorum.modulith.shared.token.AuthTokenProvider;
import de.tudl.playground.datorum.modulith.shared.token.data.Token;
import de.tudl.playground.datorum.ui.util.ModalPresenter;
import de.tudl.playground.datorum.ui.util.StageSwitcher;
import de.tudl.playground.datorum.ui.view.budget.CreateBudgetModal;
import de.tudl.playground.datorum.ui.view.login.LoginView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class MainController
{
    private final StageSwitcher stageSwitcher;
    private final ModalPresenter modalPresenter;
    private final CommandGateway commandGateway;
    private final AuthTokenProvider authTokenProvider;


    public MainController(AuthTokenProvider authTokenProvider, StageSwitcher stageSwitcher, ModalPresenter modalPresenter, CommandGateway commandGateway, QueryGateway queryGateway) {
        this.stageSwitcher = stageSwitcher;
        this.modalPresenter = modalPresenter;
        this.commandGateway = commandGateway;
        this.authTokenProvider = authTokenProvider;
    }

    public void handleLogout()
    {
        Token token;
        token = authTokenProvider.getToken();
        commandGateway.send(new LogoutUserCommand(token.username()));
    }

    public void handleBudgetCreate()
    {
        modalPresenter.showModal(CreateBudgetModal.class);
    }

    @EventListener
    private void on (LogoutEvent event)
    {
        log.info("Logout successful for user {} !", event.username());
        stageSwitcher.switchTo(LoginView.class);
    }
}
