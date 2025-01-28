package de.tudl.playground.datorum.modulith.budget.command.commands;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UpdateBudgetCommand extends ApplicationEvent
{
    private final String budgetId;

    private final String userId;

    private final String name;

    private final String description;

    private final double amount;

    public UpdateBudgetCommand(String budgetId, String userId, String name, String description, double amount) {
        super(budgetId);
        this.budgetId = budgetId;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.amount = amount;
    }
}
