package de.tudl.playground.datorum.modulith.budget.command.events;

import de.tudl.playground.datorum.modulith.eventstore.AggregateId;
import de.tudl.playground.datorum.modulith.shared.event.Event;

@Event
public record BudgetDeletedEvent(
        @AggregateId String budgetId,
        String userId,
        String budgetName,
        String budgetDescription,
        double budgetAmount
) {
}
