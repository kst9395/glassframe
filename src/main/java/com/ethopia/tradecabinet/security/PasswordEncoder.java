package com.ethopia.tradecabinet.security;

public interface PasswordEncoder {
    int PBKDF2_ITERATIONS = 100;
    int KEY_LENGTH = 128;
    String PBKDF2 = "PBKDF2WithHmacSHA1";
    String DELIMITER = ":";

    byte[] generateSalt(int size);

    byte[] encode(char[] password, byte[] salt,int iterationCount, int keyLength) throws Exception;

}
