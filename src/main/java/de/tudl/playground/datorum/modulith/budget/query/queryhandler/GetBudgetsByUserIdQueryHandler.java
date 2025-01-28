package de.tudl.playground.datorum.modulith.budget.query.queryhandler;

import de.tudl.playground.datorum.gateway.query.QueryHandler;
import de.tudl.playground.datorum.modulith.budget.command.data.Budget;
import de.tudl.playground.datorum.modulith.budget.command.data.BudgetRepository;
import de.tudl.playground.datorum.modulith.budget.query.queries.GetBudgetsByUserIdQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class GetBudgetsByUserIdQueryHandler implements QueryHandler<GetBudgetsByUserIdQuery, List<Budget>> {

    private final BudgetRepository budgetRepository;

    public GetBudgetsByUserIdQueryHandler(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public Optional<List<Budget>> handle(GetBudgetsByUserIdQuery query) {
        return budgetRepository.findBudgetsByUserId(UUID.fromString(query.userId()));
    }
}
