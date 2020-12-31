package com.ethopia.tradecabinet.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

public class PBKDF2PasswordEncoder implements PasswordEncoder {
    @Override
    public byte[] generateSalt(int size) {
        byte[] salt = new byte[size];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    @Override
    public byte[] encode(char[] password, byte[] salt, int iterationCount, int keyLength) throws Exception {
        KeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBKDF2);
        return keyFactory.generateSecret(keySpec).getEncoded();
    }
}
