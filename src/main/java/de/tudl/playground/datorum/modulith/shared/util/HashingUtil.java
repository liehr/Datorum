package de.tudl.playground.datorum.modulith.shared.util;

import de.tudl.playground.datorum.modulith.shared.exception.ErrorHashingPasswordException;
import lombok.SneakyThrows;

import java.security.MessageDigest;
import java.util.Base64;

public class HashingUtil {

    private HashingUtil()
    {
        throw new IllegalStateException("Utility class");
    }

    public static boolean verifyPassword(
            String password,
            String hash,
            String salt
    ) {
        String hashedPassword = hashPassword(password, salt);
        return hashedPassword.equals(hash);
    }

    @SneakyThrows
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes());
            byte[] hashedBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (Exception e) {
            throw new ErrorHashingPasswordException("Error hashing the password!", e);
        }
    }
}
