package com.company.Shamir;

import java.util.Random;

import static com.company.Utils.MathUtils.*;

class ShamirUser {
    private static final Random RANDOM = new Random(System.nanoTime());
    private static final int UPPER_BOUND = 32768;
    private static final int LOWER_BOUND = 1009;

    private long mod;
    private long message = 0;
    private long encodedMessage;

    private long secretFirst;
    private long secretSecond;

    ShamirUser(long mod) {
        this.mod = mod;
        generateMessage();
        generateSecret();
    }

    private void generateMessage() {
        while (!isPrime(message)) {
            message = RANDOM.nextInt(UPPER_BOUND) + LOWER_BOUND;
        }
        encodedMessage = message;
    }

    private void generateSecret() {
        int i = RANDOM.nextInt((int) (mod * 0.6)) + LOWER_BOUND;
        for (; i < mod - 1; ++i) {
            if (gcd(i, mod - 1) == 1) {
                secretFirst = i;
                break;
            }
        }
        secretSecond = modInverse(secretFirst, mod - 1);
    }

    void encodeFirstAndSendTo(ShamirUser user) {
        user.encodedMessage = modExp(encodedMessage, secretFirst, mod);
    }

    void encodeSecondAndSendTo(ShamirUser user) {
        long encoded = modExp(encodedMessage, secretSecond, mod);
        this.encodedMessage = encoded;
        user.encodedMessage = encoded;
    }

    boolean confirmMessage(ShamirUser user) {
        return this.message == user.encodedMessage;
    }

}
