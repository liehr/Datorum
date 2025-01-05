package de.tudl.playground.datorum.modulith.auth.command.commands;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LoginUserCommand extends ApplicationEvent {

    private final String username;

    private final String password;

    public LoginUserCommand(String username, String password) {
        super(username);
        this.username = username;
        this.password = password;
    }
}
