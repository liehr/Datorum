package de.tudl.playground.datorum.commandgateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DefaultCommandGateway implements CommandGateway
{
    private final ApplicationEventPublisher eventPublisher;

    public DefaultCommandGateway(ApplicationEventPublisher eventPublisher)
    {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void send(Object command)
    {
        log.info("Command received: {}", command.getClass().getSimpleName());

        eventPublisher.publishEvent(command);
    }
}
