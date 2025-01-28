package de.tudl.playground.datorum.modulith.budget.command.data.dto;

public record UpdateBudgetDto(
        String budgetId,
        String userId,
        String name,
        String description,
        double amount) { }
