package de.tudl.playground.datorum.modulith.auth.command.events;

import de.tudl.playground.datorum.modulith.shared.token.KeyManager;
import de.tudl.playground.datorum.modulith.shared.token.TokenFileService;
import de.tudl.playground.datorum.modulith.shared.token.TokenManager;
import de.tudl.playground.datorum.modulith.shared.token.data.Token;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthEventHandler
{
    private final TokenFileService tokenFileService;

    public AuthEventHandler(TokenFileService tokenFileService) {
        this.tokenFileService = tokenFileService;
    }

    @SneakyThrows
    @EventListener
    public void on (LoginSuccessfulEvent event)
    {
        String key = KeyManager.loadKey();

        Token token = TokenManager.createToken(event.username(), Collections.singletonList(event.role()), key);

        tokenFileService.writeToken(token);
    }

    @SneakyThrows
    @EventListener
    public void on (LogoutEvent event)
    {
        tokenFileService.deleteTokenFile();
    }
}
