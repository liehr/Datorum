package de.tudl.playground.datorum.modulith.auth.command.commands;

import de.tudl.playground.datorum.modulith.auth.command.aggregate.UserAggregate;
import de.tudl.playground.datorum.modulith.eventstore.EventPublisher;
import org.springframework.stereotype.Service;

@Service
public class UserCommandService
{
    private final EventPublisher eventPublisher;

    public UserCommandService(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void createUser(String userId, String name)
    {
        UserAggregate user = new UserAggregate();
        user.createUser(userId, name);

        for (Object event : user.getChanges())
            eventPublisher.publishEvent(event);
    }

    public void updateUser(String userId, String name)
    {
        UserAggregate user = new UserAggregate();
        user.updateUser(name);
    }
}
