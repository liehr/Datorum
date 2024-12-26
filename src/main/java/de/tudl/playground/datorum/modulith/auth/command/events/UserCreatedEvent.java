package de.tudl.playground.datorum.modulith.auth.command.events;

import de.tudl.playground.datorum.modulith.eventstore.AggregateId;
import lombok.Getter;

public record UserCreatedEvent(@AggregateId String userId, @Getter String name) {
}
