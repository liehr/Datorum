package de.tudl.playground.datorum.modulith.auth.command.events;

import de.tudl.playground.datorum.modulith.eventstore.AggregateId;

public record LoginFailedEvent(
        @AggregateId String username,
        String currentDate
) {}
