package de.tudl.playground.datorum.ui.controller;

import de.tudl.playground.datorum.gateway.query.QueryGateway;
import de.tudl.playground.datorum.modulith.auth.command.commands.LogoutUserCommand;
import de.tudl.playground.datorum.modulith.auth.command.events.AutomaticLoginEvent;
import de.tudl.playground.datorum.modulith.auth.command.events.LoginFailedEvent;
import de.tudl.playground.datorum.modulith.auth.command.events.LoginSuccessfulEvent;
import de.tudl.playground.datorum.modulith.budget.command.data.Budget;
import de.tudl.playground.datorum.modulith.budget.command.events.BudgetCreatedEvent;
import de.tudl.playground.datorum.modulith.budget.query.queries.GetBudgetsByUserIdQuery;
import de.tudl.playground.datorum.modulith.shared.token.AuthTokenProvider;
import de.tudl.playground.datorum.modulith.shared.token.data.Token;
import de.tudl.playground.datorum.ui.view.tile.TileComponent;
import de.tudl.playground.datorum.ui.view.tile.TileViewRegistry;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Lazy
public class DashboardController {

    private final ListProperty<TileComponent<?>> tiles = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final QueryGateway queryGateway;
    private final AuthTokenProvider authTokenProvider;
    private final TileViewRegistry tileViewRegistry;

    public DashboardController(QueryGateway queryGateway, AuthTokenProvider authTokenProvider, TileViewRegistry tileViewRegistry) {
        this.queryGateway = queryGateway;
        this.authTokenProvider = authTokenProvider;
        this.tileViewRegistry = tileViewRegistry;
    }

    /**
     * Event handler for when a new budget is created.
     */
    @EventListener
    private void on(BudgetCreatedEvent event) {
        Budget budget = new Budget();
        budget.setId(UUID.fromString(event.budgetId()));
        budget.setUserId(UUID.fromString(event.userId()));
        budget.setName(event.name());
        budget.setDescription(event.description());
        budget.setAmount(event.amount());

        TileComponent<Budget> budgetTile = new TileComponent<>(event.name(), tileViewRegistry);
        budgetTile.setData(budget);

        Platform.runLater(() -> addTile(budgetTile));
    }

    /**
     * Event handler for when a user logs in successfully.
     */
    @EventListener
    private void on(LoginSuccessfulEvent event) {
        loadBudgets();
    }

    /**
     * Event handler for when a user logs in automatically.
     */
    @EventListener
    private void on(AutomaticLoginEvent event) {
        loadBudgets();
    }

    /**
     * Event handler for when a login fails.
     */
    @EventListener
    private void on(LoginFailedEvent event) {
        removeAllTiles();
    }

    /**
     * Event handler for when a user logs out.
     */
    @EventListener
    private void on(LogoutUserCommand command) {
        removeAllTiles();
    }

    /**
     * Loads all budgets for the currently authenticated user.
     */
    public void loadBudgets() {
        Token token = authTokenProvider.getToken();
        GetBudgetsByUserIdQuery query = new GetBudgetsByUserIdQuery(token.userId().toString());

        Optional<List<Budget>> budgets = queryGateway.query(query);

        budgets.ifPresent(budgetList -> Platform.runLater(() -> {
            for (Budget budget : budgetList) {
                TileComponent<Budget> budgetTile = new TileComponent<>(budget.getName(), tileViewRegistry);
                budgetTile.setData(budget);
                addTile(budgetTile);
            }
        }));
    }

    /**
     * Adds a tile to the dashboard.
     */
    public void addTile(TileComponent<?> tileComponent) {
        Platform.runLater(() -> tiles.add(tileComponent));
    }

    /**
     * Removes a tile from the dashboard.
     */
    public void removeTile(TileComponent<?> tileComponent) {
        Platform.runLater(() -> tiles.remove(tileComponent));
    }

    /**
     * Removes all tiles from the dashboard.
     */
    public void removeAllTiles() {
        Platform.runLater(tiles::clear);
    }

    /**
     * Returns the list property of tiles.
     */
    public ListProperty<TileComponent<?>> tilesProperty() {
        return tiles;
    }
}
