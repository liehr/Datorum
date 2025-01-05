package de.tudl.playground.datorum.modulith.auth.command.events;

import de.tudl.playground.datorum.modulith.eventstore.AggregateId;

public record LoginSuccessfulEvent(
        @AggregateId String username,
        String lastLoginAttemptUsername,
        boolean lastLoginSuccess,
        String now
) {}
