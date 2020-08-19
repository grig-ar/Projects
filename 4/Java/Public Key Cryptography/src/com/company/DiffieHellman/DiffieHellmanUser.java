package com.company.DiffieHellman;

import java.util.Random;

import static com.company.Utils.MathUtils.*;

class DiffieHellmanUser {
    private static final Random RANDOM = new Random(System.nanoTime());
    private static final int UPPER_BOUND = 32768;
    private static final int LOWER_BOUND = 1009;

    private long mod;
    private long base;

    private long secret;
    private long sharedSecret;

    private long publicKey = 0;

    private void setPublicKey(long publicKey) {
        this.publicKey = publicKey;
    }

    private long getSharedSecret() {
        return sharedSecret;
    }

    DiffieHellmanUser(long mod, long base) {
        this.mod = mod;
        this.base = base;
        generateSecret();
    }

    private void generateSecret() {
        secret = 0;
        while (!isPrime(secret)) {
            secret = RANDOM.nextInt(UPPER_BOUND) + LOWER_BOUND;
        }
    }

    void sendPublicKeyTo(DiffieHellmanUser user) {
        long publicKey = modExp(base, secret, mod);
        user.setPublicKey(publicKey);
        user.generateSharedSecret();
    }

    private void generateSharedSecret() {
        assert publicKey != 0;
        sharedSecret = modExp(publicKey, secret, mod);
    }

    boolean confirmSharedSecret(DiffieHellmanUser user) {
        return this.getSharedSecret() == user.getSharedSecret();
    }

}
