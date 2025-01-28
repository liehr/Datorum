package de.tudl.playground.datorum.modulith.budget.command.events;

import de.tudl.playground.datorum.modulith.budget.command.data.Budget;
import de.tudl.playground.datorum.modulith.budget.command.data.BudgetRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BudgetEventHandler {
    private final BudgetRepository budgetRepository;

    public BudgetEventHandler(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @EventListener
    public void on(BudgetCreatedEvent event) {
        Budget budget = new Budget();
        budget.setId(UUID.fromString(event.budgetId()));
        budget.setUserId(UUID.fromString(event.userId()));
        budget.setName(event.name());
        budget.setDescription(event.description());
        budget.setAmount(event.amount());

        budgetRepository.save(budget);
    }

    @EventListener
    public void on(BudgetUpdatedEvent event)
    {
        Budget budget = budgetRepository.findById(UUID.fromString(event.budgetId()))
                .orElseThrow(() -> new IllegalArgumentException("Budget not found"));

        budget.setUserId(UUID.fromString(event.userId()));
        budget.setName(event.name());
        budget.setDescription(event.description());
        budget.setAmount(event.amount());

        budgetRepository.save(budget);
    }

    @EventListener
    public void on(BudgetDeletedEvent event)
    {
        Budget budget = budgetRepository.findById(UUID.fromString(event.budgetId()))
                .orElseThrow(() -> new IllegalArgumentException("Budget not found"));

        budgetRepository.delete(budget);
    }
}
