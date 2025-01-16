package de.tudl.playground.datorum.modulith.shared.token;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.tudl.playground.datorum.modulith.shared.token.data.Token;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Service class for managing the token file ({@code auth.json}) used for authentication purposes.
 * <p>
 * This class provides methods to write, read, delete, and check the existence of the token file.
 * It ensures proper handling of the token's lifecycle and facilitates secure token storage.
 * The token file is stored in the {@code .datorum} directory within the user's application data directory
 * as determined by the {@code APPDATA} environment variable.
 * </p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Writes a {@link Token} object to a JSON file.</li>
 *     <li>Reads and deserializes a {@link Token} object from the JSON file.</li>
 *     <li>Deletes the token file if it exists.</li>
 *     <li>Checks for the presence of the token file.</li>
 * </ul>
 *
 * <h2>Environment Variable Dependency:</h2>
 * This class relies on the {@code APPDATA} environment variable to determine the directory
 * where the token file ({@code auth.json}) will be stored.
 *
 * <h2>Thread Safety:</h2>
 * This class is not thread-safe as it performs file I/O operations. Synchronization may be required
 * if accessed concurrently by multiple threads.
 *
 * @see Token
 * @see Gson
 */
@Service
public class TokenFileService {

    private static final String APPDATA = System.getenv("APPDATA");
    private static final String DIRECTORY_NAME = ".datorum";
    private static final String AUTH_JSON_FILE = "auth.json";
    private static final Path TOKEN_DIRECTORY_PATH = Paths.get(APPDATA, DIRECTORY_NAME);
    private static final Path TOKEN_FILE_PATH = TOKEN_DIRECTORY_PATH.resolve(AUTH_JSON_FILE);
    private final Gson gson = new Gson();

    public TokenFileService() throws IOException {
        ensureDirectoryExists();
    }

    /**
     * Ensures the token directory exists. If it does not, it is created.
     *
     * @throws IOException If the directory cannot be created.
     */
    private void ensureDirectoryExists() throws IOException {
        if (Files.notExists(TOKEN_DIRECTORY_PATH)) {
            Files.createDirectories(TOKEN_DIRECTORY_PATH);
        }
    }

    /**
     * Writes a {@link Token} object to the {@code auth.json} file.
     *
     * @param token The Token object to write.
     * @throws IOException If writing to the file fails.
     */
    public void writeToken(Token token) throws IOException {
        ensureDirectoryExists();
        String json = gson.toJson(token);
        Files.writeString(TOKEN_FILE_PATH, json);
    }

    /**
     * Reads and deserializes the {@link Token} object from the {@code auth.json} file.
     *
     * @return An Optional containing the Token if it exists and is valid, or empty if not.
     */
    public Optional<Token> readToken() {
        try {
            if (Files.exists(TOKEN_FILE_PATH)) {
                String json = Files.readString(TOKEN_FILE_PATH);
                return Optional.of(gson.fromJson(json, Token.class));
            }
        } catch (IOException | JsonSyntaxException e) {
            // Log the error or handle it appropriately
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Deletes the {@code auth.json} file.
     *
     * @throws IOException If deleting the file fails.
     */
    public void deleteTokenFile() throws IOException {
        if (Files.exists(TOKEN_FILE_PATH)) {
            Files.delete(TOKEN_FILE_PATH);
        }
    }

    /**
     * Checks if the {@code auth.json} file exists.
     *
     * @return true if the file exists, false otherwise.
     */
    public boolean isTokenFilePresent() {
        return Files.exists(TOKEN_FILE_PATH);
    }
}
