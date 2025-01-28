package de.tudl.playground.datorum.modulith.auth.command.commands;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LogoutUserCommand extends ApplicationEvent
{
    private final String username;

    public LogoutUserCommand(String username) {
        super(username);
        this.username = username;
    }
}
