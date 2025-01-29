package de.tudl.playground.datorum.modulith.shared.token;

import de.tudl.playground.datorum.modulith.auth.command.events.AutomaticLoginEvent;
import de.tudl.playground.datorum.modulith.auth.command.events.LoginSuccessfulEvent;
import de.tudl.playground.datorum.modulith.eventstore.EventPublisher;
import de.tudl.playground.datorum.modulith.shared.token.data.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service class for validating authentication tokens.
 * <p>
 * The {@code TokenValidationService} is responsible for checking the validity of authentication tokens
 * by using the {@link TokenFileService}, {@link TokenManager}, and {@link KeyManager}.
 * It ensures that the token exists, is not expired, and its signature is valid.
 * </p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Loads the secret key using the {@link KeyManager}.</li>
 *     <li>Checks for the presence of the token file using the {@link TokenFileService}.</li>
 *     <li>Validates the token's signature and expiration using the {@link TokenManager}.</li>
 * </ul>
 *
 * <h2>Thread Safety:</h2>
 * This class is not inherently thread-safe as it relies on external services and file I/O.
 * Ensure external dependencies are thread-safe if concurrent access is expected.
 *
 * <h2>Methods:</h2>
 * <ul>
 *     <li>{@link #isValidToken()}: Validates the token by ensuring it exists, is signed correctly, and is not expired.</li>
 * </ul>
 *
 * <h2>Dependencies:</h2>
 * <ul>
 *     <li>{@link TokenFileService}: Handles file operations related to the token.</li>
 *     <li>{@link KeyManager}: Loads or generates the cryptographic key used for token validation.</li>
 *     <li>{@link TokenManager}: Validates the token's signature and expiration.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * TokenValidationService validationService = new TokenValidationService(new TokenFileService());
 * boolean isValid = validationService.isValidToken();
 * System.out.println("Is the token valid? " + isValid);
 * </pre>
 *
 * <h2>Exception Handling:</h2>
 * <ul>
 *     <li>{@link IOException}: Thrown if an error occurs while loading the key or reading the token file.</li>
 *     <li>{@link RuntimeException}: Other exceptions may be wrapped in {@link IOException} for consistency.</li>
 * </ul>
 *
 * <h2>Design:</h2>
 * This class is a Spring-managed {@link Component}, which allows it to be injected as a dependency into other
 * parts of the application where token validation is required.
 *
 * <h2>Security Considerations:</h2>
 * <ul>
 *     <li>Ensure that the secret key and token file are stored securely and accessed only by authorized parties.</li>
 *     <li>Periodically rotate the secret key and tokens to enhance security.</li>
 *     <li>Ensure the system clock is synchronized to avoid false validation failures due to expiration checks.</li>
 * </ul>
 *
 * @see TokenFileService
 * @see KeyManager
 * @see TokenManager
 * @see Token
 */
@Slf4j
@Component
@Scope("singleton")
public class TokenValidationService {

    private final TokenFileService tokenFileService;
    private final AuthTokenProvider authTokenProvider;
    private final EventPublisher eventPublisher;

    public TokenValidationService(TokenFileService tokenFileService, AuthTokenProvider authTokenProvider, EventPublisher eventPublisher) {
        this.tokenFileService = tokenFileService;
        this.authTokenProvider = authTokenProvider;
        this.eventPublisher = eventPublisher;
    }

    public boolean isValidToken() throws IOException {
        try {
            String key = KeyManager.loadKey();
            if (tokenFileService.isTokenFilePresent()) {
                Optional<Token> token = tokenFileService.readToken();

                if (token.isPresent() && TokenManager.validateToken(token.get(), key))
                    {
                        authTokenProvider.setToken(token.get());
                        eventPublisher.publishEvent(new AutomaticLoginEvent(
                                token.get().userId().toString(),
                                token.get().username(),
                                token.get().roles(),
                                token.get().signature()
                        ));

                        return true;
                    }
            }
        } catch (Exception e) {
            throw new IOException("Cannot load key or token from file!", e);
        }
        return false;
    }
}

