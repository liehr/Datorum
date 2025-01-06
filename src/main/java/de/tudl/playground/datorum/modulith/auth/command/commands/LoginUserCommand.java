package de.tudl.playground.datorum.modulith.auth.command.commands;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * The {@code LoginUserCommand} class represents a command for initiating a user login attempt.
 * It encapsulates the necessary credentials, including the username and password,
 * and serves as an event in Spring's application event model.
 *
 * <p>This command is typically published to trigger login handling logic within
 * the {@link de.tudl.playground.datorum.modulith.auth.command.commands.AuthCommandHandler}.</p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Encapsulating the username and password for a login attempt.</li>
 *     <li>Serving as an event to initiate the login process.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * LoginUserCommand command = new LoginUserCommand("username", "password");
 * applicationEventPublisher.publishEvent(command);
 * }</pre>
 *
 * @see de.tudl.playground.datorum.modulith.auth.command.commands.AuthCommandHandler
 */
@Getter
public class LoginUserCommand extends ApplicationEvent {

    private final String username;
    private final String password;

    /**
     * Constructs a {@code LoginUserCommand} with the given username and password.
     *
     * @param username the username for the login attempt.
     * @param password the password for the login attempt.
     */
    public LoginUserCommand(String username, String password) {
        super(username);
        this.username = username;
        this.password = password;
    }
}

