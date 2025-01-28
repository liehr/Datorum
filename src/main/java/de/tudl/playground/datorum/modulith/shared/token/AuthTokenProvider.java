package de.tudl.playground.datorum.modulith.shared.token;

import de.tudl.playground.datorum.modulith.shared.token.data.Token;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@Scope("singleton")
public class AuthTokenProvider
{
    private Token token;
}
