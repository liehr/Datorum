package de.tudl.playground.datorum.modulith.auth.command.events;

import de.tudl.playground.datorum.modulith.eventstore.AggregateId;

public record LogoutEvent(@AggregateId String username) {
}
