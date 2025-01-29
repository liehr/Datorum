package de.tudl.playground.datorum.modulith.auth.command.events;

import de.tudl.playground.datorum.modulith.eventstore.AggregateId;

import java.util.List;

public record AutomaticLoginEvent(
        @AggregateId String userId,
        String username,
        List<String> roles,
        String signature
) {
}
