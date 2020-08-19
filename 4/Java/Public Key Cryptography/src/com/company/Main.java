package com.company;

import com.company.DiffieHellman.DiffieHellmanKeyExchange;
import com.company.Shamir.ShamirKeyExchange;

public class Main {

    public static void main(String[] args) {
        var test = new ShamirKeyExchange();
        //var test = new DiffieHellmanKeyExchange();
        test.exchangeKeys();

    }
}
