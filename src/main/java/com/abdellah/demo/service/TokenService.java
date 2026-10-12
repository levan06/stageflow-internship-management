package com.abdellah.demo.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service 
public class TokenService 
{
    // Using SecureRandom for cryptographically secure and unpredictable values.
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /* Generating a generic secure token */
    public String generateSecureToken()
    {
        byte[] tokenBytes = new byte[32];
        SECURE_RANDOM.nextBytes(tokenBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(tokenBytes);
    }

    /* Generating the CSRF Token */
    public String generateCsrfToken()
    {
        return generateSecureToken();
    }
    

    /**
     * Hashes both inputs to a fixed 32-byte size before running MessageDigest.isEqual.
     * This safely protects against length-leaking timing attacks.
     */
    public boolean secureTokenEquals(String expected, String submitted)
    {
        if (expected == null || submitted == null) 
            {
            return false;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] expectedHash  = md.digest(expected.getBytes(StandardCharsets.UTF_8));
            byte[] submittedHash = md.digest(submitted.getBytes(StandardCharsets.UTF_8));
            
            return MessageDigest.isEqual(expectedHash, submittedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm is missing from JVM execution environment", e);
        }
    }
}
