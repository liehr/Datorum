package de.tudl.playground.datorum.gateway.command;

/**
 * The {@code CommandGateway} interface defines a contract for sending commands in the application.
 * <p>
 * This interface is used to send commands from the client or other parts of the system to the appropriate
 * command handlers. The {@code send} method takes an {@link Object} as a parameter, which represents
 * a command that encapsulates business logic that needs to be executed.
 * </p>
 * <p>
 * The implementation of this interface is responsible for dispatching the commands to the correct handler
 * or processing mechanism, depending on the system's architecture (e.g., event-driven or CQRS-based system).
 * </p>
 */
public interface CommandGateway {

    /**
     * Sends a command to the appropriate command handler for processing.
     * <p>
     * The command is passed as an argument, and its processing will depend on the business logic implemented
     * in the corresponding command handler. The {@code send} method may trigger side effects or state transitions
     * as defined by the command handler.
     * </p>
     *
     * @param command the command to be sent and processed.
     */
    void send(Object command);
}
