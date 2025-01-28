package de.tudl.playground.datorum.modulith.budget.command.aggregate;

import de.tudl.playground.datorum.modulith.budget.command.data.dto.CreateBudgetDto;
import de.tudl.playground.datorum.modulith.budget.command.events.BudgetCreatedEvent;
import de.tudl.playground.datorum.modulith.eventstore.EventStore;
import de.tudl.playground.datorum.modulith.eventstore.service.EventProcessorService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BudgetAggregate
{
    private final EventProcessorService eventProcessorService;

    private String budgetId;

    @Getter String userId;

    @Getter
    private String budgetName;

    @Getter
    private String budgetDescription;

    @Getter
    private double budgetValue;

    @Getter
    private final List<Object> changes = new ArrayList<>();

    public BudgetAggregate(EventProcessorService eventProcessorService) {
        this.eventProcessorService = eventProcessorService;
    }

    public void createBudget(CreateBudgetDto createBudgetDto) {
        if (budgetId != null) {
            throw new IllegalStateException("Budget already exists");
        }

        apply(new BudgetCreatedEvent(
                createBudgetDto.budgetId(),
                createBudgetDto.userId(),
                createBudgetDto.name(),
                createBudgetDto.description(),
                createBudgetDto.amount()
        ));
    }

    private void apply(Object event) {
        this.changes.add(event);
        applyEvent(event);
    }

    private void applyEvent(Object event) {
        if (event instanceof BudgetCreatedEvent budgetCreatedEvent) {
            this.budgetId = budgetCreatedEvent.budgetId();
            this.userId = budgetCreatedEvent.userId();
            this.budgetName = budgetCreatedEvent.name();
            this.budgetDescription = budgetCreatedEvent.description();
            this.budgetValue = budgetCreatedEvent.amount();
        }
    }

    private void rehydrate(List<EventStore> events)
    {
        events.forEach(eventStore -> eventProcessorService.processEvent(eventStore, this::applyEvent));
    }

}
