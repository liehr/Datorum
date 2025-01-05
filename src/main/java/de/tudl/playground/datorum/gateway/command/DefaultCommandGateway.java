package de.tudl.playground.datorum.gateway.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * The {@code DefaultCommandGateway} is the default implementation of the {@link CommandGateway} interface.
 * <p>
 * This service is responsible for sending commands to the application by publishing them as events in
 * the Spring application context. It acts as a dispatcher that takes a command, logs the event, and
 * then forwards it to the Spring {@link ApplicationEventPublisher} for further processing.
 * </p>
 * <p>
 * The {@code DefaultCommandGateway} does not contain business logic for handling the commands; instead,
 * it delegates the processing to other components or services that listen to the events.
 * </p>
 */
@Service
@Slf4j
public class DefaultCommandGateway implements CommandGateway {

    /**
     * The {@link ApplicationEventPublisher} used to publish the command as an event within the Spring context.
     * The event can then be consumed by other components or services.
     */
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Constructs a {@code DefaultCommandGateway} with the given {@link ApplicationEventPublisher}.
     *
     * @param eventPublisher the event publisher used to broadcast the command as an event.
     */
    public DefaultCommandGateway(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * Sends the provided command by publishing it as an event.
     * <p>
     * The method logs the received command and then uses the {@link ApplicationEventPublisher} to
     * broadcast the command as an event within the application context.
     * </p>
     *
     * @param command the command to be sent, typically representing a request for business logic to be executed.
     */
    @Override
    public void send(Object command) {
        if (command == null) throw new IllegalArgumentException(
                "Command cannot be null"
        );

        // Publish the command as an event to be handled by listeners
        eventPublisher.publishEvent(command);
    }
}
