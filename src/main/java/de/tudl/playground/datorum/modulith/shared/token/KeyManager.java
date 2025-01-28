package de.tudl.playground.datorum.modulith.shared.token;

import lombok.SneakyThrows;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Utility class for managing application keys used in cryptographic operations.
 * <p>
 * The {@code KeyManager} handles loading and generating a secret key stored in a file.
 * It ensures that the application has a consistent key for operations such as token validation.
 * The key is stored as a Base64-encoded string in the {@code ~/.datorum} directory,
 * where {@code ~} represents the user's home directory.
 * </p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Loads an existing key from a file, or generates a new one if the file does not exist.</li>
 *     <li>Uses HMAC-SHA256 for key generation with a key size of 256 bits.</li>
 *     <li>Stores the generated key securely in the {@code ~/.datorum} directory.</li>
 * </ul>
 *
 * <h2>Cross-Platform Compatibility:</h2>
 * This class determines the directory for storing the key file based on the {@code user.home} system property,
 * ensuring compatibility across Windows, Linux, and macOS.
 *
 * <h2>Thread Safety:</h2>
 * This utility class is thread-safe as it does not maintain any mutable state.
 *
 * <h2>Methods:</h2>
 * <ul>
 *     <li>{@link #loadKey()}: Loads the key from the file or generates a new one if the file is missing.</li>
 *     <li>{@link #generateKey()}: Generates a new cryptographic key and saves it to the file.</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * String key = KeyManager.loadKey(); // Load or generate the application key
 * System.out.println("Application Key: " + key);
 * </pre>
 *
 * <h2>Exceptions:</h2>
 * This class uses the {@code @SneakyThrows} annotation from Lombok to handle checked exceptions
 * implicitly. Exceptions like {@code IOException} may occur when reading or writing the key file.
 */
public class KeyManager {
    private static final String DIRECTORY_NAME = ".datorum";
    private static final String KEY_FILE = "app.key";
    private static final Path KEY_DIRECTORY_PATH = Paths.get(System.getProperty("user.home"), DIRECTORY_NAME);
    private static final Path KEY_FILE_PATH = KEY_DIRECTORY_PATH.resolve(KEY_FILE);

    private KeyManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Loads the secret key from the file or generates a new one if the file does not exist.
     *
     * @return The Base64-encoded secret key.
     */
    @SneakyThrows
    public static String loadKey() {
        ensureDirectoryExists();

        if (Files.notExists(KEY_FILE_PATH)) {
            return generateKey();
        }
        return Files.readString(KEY_FILE_PATH);
    }

    /**
     * Generates a new secret key, stores it in the key file, and returns it.
     *
     * @return The Base64-encoded secret key.
     */
    @SneakyThrows
    public static String generateKey() {
        ensureDirectoryExists();

        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(256);
        SecretKey key = keyGen.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        Files.writeString(KEY_FILE_PATH, encodedKey);
        return encodedKey;
    }

    /**
     * Ensures the key directory exists. If it does not, it is created.
     */
    @SneakyThrows
    private static void ensureDirectoryExists() {
        if (Files.notExists(KEY_DIRECTORY_PATH)) {
            Files.createDirectories(KEY_DIRECTORY_PATH);
        }
    }
}
