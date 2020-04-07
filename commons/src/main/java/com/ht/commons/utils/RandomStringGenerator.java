package com.ht.commons.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 随机字符串生成器，用于生成authorization_code
 */
public class RandomStringGenerator {
    private static final char[] DEFAULT_CODEC = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            .toCharArray();
    private Random random = new SecureRandom();

    private int length;

    /**
     * Create a generator with the default length (6).
     */
    public RandomStringGenerator() {
        this(6);
    }

    public RandomStringGenerator(int length) {
        this.length = length;
    }

    public String generate() {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        char[] randomChar = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            randomChar[i] = DEFAULT_CODEC[(bytes[i] & 0xFF) % 62];
        }
        return new String(randomChar);
    }

}
