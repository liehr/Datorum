package de.tudl.playground.datorum.modulith.auth.command.events;

import de.tudl.playground.datorum.modulith.eventstore.AggregateId;
import de.tudl.playground.datorum.modulith.shared.event.Event;

@Event
public record LoginSuccessfulEvent(
        @AggregateId String username,
        String userId,
        String role,
        String currentDate
) {}
