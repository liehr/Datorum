package de.tudl.playground.datorum.modulith.auth.command.events;

import de.tudl.playground.datorum.modulith.eventstore.AggregateId;
import de.tudl.playground.datorum.modulith.shared.event.Event;

@Event
public record LoginFailedEvent(
        @AggregateId String username,
        String currentDate
) {}
