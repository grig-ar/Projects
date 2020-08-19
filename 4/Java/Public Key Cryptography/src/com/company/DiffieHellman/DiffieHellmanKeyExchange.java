package com.company.DiffieHellman;

public class DiffieHellmanKeyExchange {
    private static final long MOD = 33629;
    private static final long BASE = 10003;

    private DiffieHellmanUser alice = new DiffieHellmanUser(MOD, BASE);
    private DiffieHellmanUser bob = new DiffieHellmanUser(MOD, BASE);

    public void exchangeKeys() {
        alice.sendPublicKeyTo(bob);
        bob.sendPublicKeyTo(alice);

        if (alice.confirmSharedSecret(bob)) {
            System.out.println("Alice confirmed");
        }

        if (bob.confirmSharedSecret(alice)) {
            System.out.println("Bob confirmed");
        }
    }
}
