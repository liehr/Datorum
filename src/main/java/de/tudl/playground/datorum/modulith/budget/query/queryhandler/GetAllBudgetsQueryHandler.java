package de.tudl.playground.datorum.modulith.budget.query.queryhandler;

import de.tudl.playground.datorum.gateway.query.QueryHandler;
import de.tudl.playground.datorum.modulith.budget.command.data.Budget;
import de.tudl.playground.datorum.modulith.budget.command.data.BudgetRepository;
import de.tudl.playground.datorum.modulith.budget.query.queries.GetAllBudgetsQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GetAllBudgetsQueryHandler implements QueryHandler<GetAllBudgetsQuery, List<Budget>> {

    private final BudgetRepository budgetRepository;

    public GetAllBudgetsQueryHandler(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public Optional<List<Budget>> handle(GetAllBudgetsQuery query) {
        return Optional.of(budgetRepository.findAll());
    }
}
