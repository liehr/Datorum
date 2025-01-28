package de.tudl.playground.datorum.modulith.budget.command.commands;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteBudgetCommand extends ApplicationEvent {

    private final String budgetId;

    public DeleteBudgetCommand(String budgetId) {
        super(budgetId);
        this.budgetId = budgetId;
    }
}
