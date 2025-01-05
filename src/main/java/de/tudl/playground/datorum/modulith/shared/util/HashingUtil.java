package de.tudl.playground.datorum.modulith.shared.util;

import java.security.MessageDigest;
import java.util.Base64;

public class HashingUtil {

    public static boolean verifyPassword(
            String password,
            String hash,
            String salt
    ) {
        String hashedPassword = hashPassword(password, salt);
        return hashedPassword.equals(hash);
    }

    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt.getBytes());
            byte[] hashedBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing the password!", e);
        }
    }
}
