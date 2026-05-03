package com.taskify;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class GenerateSecretKey {
    public static void generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = keyGen.generateKey();
            String secretKeyGen = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            System.out.println("Your Secret Key is => "+secretKeyGen);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        generateSecretKey();
    }
}
