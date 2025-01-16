package de.tudl.playground.datorum.modulith.shared.token;

import de.tudl.playground.datorum.modulith.shared.token.data.Token;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthTokenProvider
{
    private Token token;

    private final TokenValidationService tokenValidationService;

    private final TokenFileService tokenFileService;

    public AuthTokenProvider(TokenValidationService tokenValidationService, TokenFileService tokenFileService) {
        this.tokenValidationService = tokenValidationService;
        this.tokenFileService = tokenFileService;
    }

    @SneakyThrows
    public Token getToken() {
        if (tokenValidationService.isValidToken()) {
            token = tokenFileService.readToken().orElse(token);
        }
        return token;
    }
}
