package com.company.Shamir;

public class ShamirKeyExchange {
    private static final long MOD = 33629;

    private ShamirUser alice = new ShamirUser(MOD);
    private ShamirUser bob = new ShamirUser(MOD);

    public void exchangeKeys() {
        alice.encodeFirstAndSendTo(bob);
        bob.encodeFirstAndSendTo(alice);
        alice.encodeSecondAndSendTo(bob);
        bob.encodeSecondAndSendTo(alice);

        if (alice.confirmMessage(bob)) {
            System.out.println("Alice confirmed");
        }

    }


}
