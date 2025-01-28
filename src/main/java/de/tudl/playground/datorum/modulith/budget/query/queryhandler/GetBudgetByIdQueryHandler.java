package de.tudl.playground.datorum.modulith.budget.query.queryhandler;

import de.tudl.playground.datorum.gateway.query.QueryHandler;
import de.tudl.playground.datorum.modulith.budget.command.data.Budget;
import de.tudl.playground.datorum.modulith.budget.command.data.BudgetRepository;
import de.tudl.playground.datorum.modulith.budget.query.queries.GetBudgetByIdQuery;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class GetBudgetByIdQueryHandler implements QueryHandler<GetBudgetByIdQuery, Budget> {

    private final BudgetRepository budgetRepository;

    public GetBudgetByIdQueryHandler(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public Optional<Budget> handle(GetBudgetByIdQuery query) {
        return budgetRepository.findById(UUID.fromString(query.budgetId()));
    }
}
