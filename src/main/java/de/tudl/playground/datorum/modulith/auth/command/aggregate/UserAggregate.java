package de.tudl.playground.datorum.modulith.auth.command.aggregate;

import de.tudl.playground.datorum.modulith.auth.command.events.UserCreatedEvent;
import de.tudl.playground.datorum.modulith.auth.command.events.UserUpdatedEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class UserAggregate
{
    private String userId;

    @Getter
    private String name;

    @Getter
    private List<Object> changes = new ArrayList<>();

    public void createUser(String userId, String name) {
        if (this.userId != null)
            throw new IllegalArgumentException("User already exist!");

        apply(new UserCreatedEvent(userId, name));
    }

    public void updateUser(String name)
    {
        if (this.userId == null)
            throw new IllegalArgumentException("User does not exist!");

        apply(new UserUpdatedEvent(this.userId, name));
    }

    private void apply(Object event)
    {
        this.changes.add(event);

        applyEvent(event);
    }

    private void applyEvent(Object event)
    {
        if (event instanceof UserCreatedEvent)
        {
            this.userId = ((UserCreatedEvent) event).userId();
            this.name = ((UserCreatedEvent) event).name();
        }

        else if(event instanceof UserUpdatedEvent)
        {
            this.name = ((UserUpdatedEvent) event).name();
        }
    }

    public void rehydrate(List<Object> events)
    {
        for (Object event : events)
            applyEvent(event);
    }
}
