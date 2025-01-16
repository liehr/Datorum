package de.tudl.playground.datorum.ui.controller;

import de.tudl.playground.datorum.modulith.shared.token.AuthTokenProvider;
import de.tudl.playground.datorum.modulith.shared.token.data.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MainController
{
    public MainController(AuthTokenProvider authTokenProvider) {

        Token token = authTokenProvider.getToken();


    }
}
