package de.tudl.playground.datorum.modulith.auth.command.events;

import de.tudl.playground.datorum.modulith.eventstore.AggregateId;
import lombok.Getter;
import lombok.Setter;

@Getter
public final class ValidateCredentialsEvent {

    private final String username;

    private final String password;

    @Setter
    private boolean success;

    public ValidateCredentialsEvent(
            String username,
            String password,
            boolean success
    ) {
        this.username = username;
        this.password = password;
        this.success = success;
    }

    @AggregateId
    public String username() {
        return username;
    }
}
