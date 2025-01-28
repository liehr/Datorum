package de.tudl.playground.datorum.modulith.budget.command.commands;

import de.tudl.playground.datorum.modulith.budget.command.aggregate.BudgetAggregate;
import de.tudl.playground.datorum.modulith.budget.command.data.dto.CreateBudgetDto;
import de.tudl.playground.datorum.modulith.eventstore.EventPublisher;
import de.tudl.playground.datorum.modulith.eventstore.EventStoreRepository;
import de.tudl.playground.datorum.modulith.eventstore.service.EventProcessorService;
import de.tudl.playground.datorum.modulith.shared.event.Event;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class BudgetCommandHandler
{
    private final EventStoreRepository eventStoreRepository;

    private final EventPublisher eventPublisher;

    private final EventProcessorService eventProcessorService;

    public BudgetCommandHandler(EventStoreRepository eventStoreRepository, EventPublisher eventPublisher, EventProcessorService eventProcessorService) {
        this.eventStoreRepository = eventStoreRepository;
        this.eventPublisher = eventPublisher;
        this.eventProcessorService = eventProcessorService;
    }

    @EventListener
    public void handle(CreateBudgetCommand command)
    {
        BudgetAggregate aggregate = new BudgetAggregate(eventProcessorService);

        CreateBudgetDto createDto = new CreateBudgetDto(
                command.getBudgetId(),
                command.getUserId(),
                command.getName(),
                command.getDescription(),
                command.getAmount()
        );

        aggregate.createBudget(createDto);

        for (Object event : aggregate.getChanges())
        {
            eventPublisher.publishEvent(event);
        }
    }
}
