package com.onlineexam.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Salted, iterated SHA-256 password hashing using only the JDK (no external
 * libraries). Each password gets a fresh 16-byte random salt and is stretched
 * over {@value #ITERATIONS} SHA-256 rounds. The stored value has the form
 * {@code base64(salt):base64(hash)}.
 *
 * <p>Storing a salted, stretched hash (instead of the plaintext password used
 * in the original project) defends against rainbow-table and brute-force
 * attacks even if the database is leaked.</p>
 */
public final class PasswordUtil {

    /** Number of SHA-256 rounds. Must match the value used to seed database.sql. */
    private static final int ITERATIONS = 100_000;
    private static final int SALT_BYTES = 16;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
        // utility class - no instances
    }

    /** Hash a plaintext password for storage. */
    public static String hash(String password) {
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] digest = stretch(salt, password);
        return Base64.getEncoder().encodeToString(salt)
                + ":" + Base64.getEncoder().encodeToString(digest);
    }

    /** Verify a plaintext password against a previously stored hash. */
    public static boolean verify(String password, String stored) {
        if (stored == null || !stored.contains(":")) {
            return false;
        }
        String[] parts = stored.split(":", 2);
        try {
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expected = Base64.getDecoder().decode(parts[1]);
            byte[] actual = stretch(salt, password);
            // constant-time comparison to avoid timing attacks
            return MessageDigest.isEqual(expected, actual);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /** digest = SHA-256(salt || password), then hashed ITERATIONS-1 more times. */
    private static byte[] stretch(byte[] salt, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            md.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();               // resets md afterwards
            for (int i = 1; i < ITERATIONS; i++) {
                digest = md.digest(digest);            // SHA-256 of previous digest
            }
            return digest;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}
