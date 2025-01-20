package de.tudl.playground.datorum.modulith.shared.token;

import de.tudl.playground.datorum.modulith.shared.token.data.Token;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope("singleton")
public class AuthTokenProvider
{
    @Getter
    @Setter
    private Token token;
}
