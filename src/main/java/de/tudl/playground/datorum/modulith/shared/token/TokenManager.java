package de.tudl.playground.datorum.modulith.shared.token;

import com.google.gson.Gson;
import de.tudl.playground.datorum.modulith.shared.token.data.Token;
import lombok.SneakyThrows;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;

/**
 * Utility class for creating and validating authentication tokens.
 * <p>
 * The {@code TokenManager} is responsible for generating cryptographically signed tokens
 * and validating their authenticity and expiration status. It uses HMAC-SHA256 for
 * generating the token signatures to ensure data integrity.
 * </p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Generates tokens with embedded user information, roles, expiration, and a cryptographic signature.</li>
 *     <li>Validates tokens by verifying the signature and checking the expiration date.</li>
 * </ul>
 *
 * <h2>Token Structure:</h2>
 * A token consists of the following components:
 * <ul>
 *     <li>Username: The identity of the user.</li>
 *     <li>Roles: A list of roles associated with the user.</li>
 *     <li>Expiration Date: The date and time when the token expires.</li>
 *     <li>Signature: A cryptographic hash of the token data to ensure integrity.</li>
 * </ul>
 *
 * <h2>Thread Safety:</h2>
 * This class is thread-safe as it does not maintain any mutable state and only provides static methods.
 *
 * <h2>Methods:</h2>
 * <ul>
 *     <li>{@link #createToken(String, List, String)}: Creates a token with user details, roles, and a cryptographic signature.</li>
 *     <li>{@link #validateToken(Token, String)}: Validates the authenticity and expiration of a token.</li>
 * </ul>
 *
 * <h2>Dependencies:</h2>
 * <ul>
 *     <li>{@link Gson}: Used for serializing and deserializing token payloads to and from JSON.</li>
 *     <li>{@code javax.crypto.Mac}: Used for generating HMAC-SHA256 signatures.</li>
 *     <li>{@link Token}: Represents the token data structure.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * String secretKey = KeyManager.loadKey();
 *
 * // Creating a token
 * List<String> roles = List.of("USER", "ADMIN");
 * Token token = TokenManager.createToken("username", roles, secretKey);
 *
 * // Validating a token
 * boolean isValid = TokenManager.validateToken(token, secretKey);
 * System.out.println("Token is valid: " + isValid);
 * </pre>
 *
 * <h2>Exception Handling:</h2>
 * <ul>
 *     <li>{@link IllegalStateException}: Thrown when attempting to instantiate the utility class.</li>
 *     <li>{@link java.security.InvalidKeyException}: Thrown if the secret key is invalid.</li>
 * </ul>
 *
 * <h2>Security Considerations:</h2>
 * <ul>
 *     <li>The secret key must be securely stored and never exposed to unauthorized parties.</li>
 *     <li>Ensure the system clock is synchronized to avoid issues with token expiration validation.</li>
 * </ul>
 *
 * <h2>Design:</h2>
 * This class is designed as a utility with a private constructor to prevent instantiation.
 * It provides static methods for token creation and validation.
 *
 * @see Token
 * @see KeyManager
 * @see Gson
 * @see Mac
 */
public class TokenManager {
    private static final String HMAC_ALGO = "HmacSHA256";
    private static final Gson gson = new Gson();

    private TokenManager()
    {
        throw new IllegalStateException("Utility class");
    }

    public static Token createToken(String username, List<String> roles, String secretKey) {
        LocalDateTime expirationDate = LocalDateTime.ofInstant(
                Instant.now().plusSeconds(43200), // 12-hour expiration
                ZoneId.systemDefault()
        );

        String payload = gson.toJson(new Token(username, roles, expirationDate.toString(), ""));
        String signature = sign(payload, secretKey);

        return new Token(username, roles, expirationDate.toString(), signature);
    }

    public static boolean validateToken(Token token, String secretKey) {
        String payload = gson.toJson(new Token(token.username(), token.Roles(), token.expirationDate(), ""));
        String expectedSignature = sign(payload, secretKey);

        return expectedSignature.equals(token.signature()) &&
                LocalDateTime.parse(token.expirationDate()).isAfter(LocalDateTime.now());
    }

    @SneakyThrows
    private static String sign(String data, String secretKey) {
        Mac mac = Mac.getInstance(HMAC_ALGO);
        mac.init(new SecretKeySpec(Base64.getDecoder().decode(secretKey), HMAC_ALGO));
        return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes()));
    }
}
