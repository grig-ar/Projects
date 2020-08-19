package com.company.Utils;

public final class MathUtils {

    public static boolean isPrime(long n) {

        if (n <= 1) {
            return false;
        }

        if (n <= 3) {
            return true;
        }

        if ((n & 1) == 0 || n % 3 == 0) {
            return false;
        }

        for (int i = 5; (i * i) <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }

        return true;
    }

    public static long modExp(long number, long exponent, long mod) {

        if (mod <= 0) {
            throw new ArithmeticException("Mod not positive");
        }
        if (exponent < 0) {
            throw new ArithmeticException("Negative exponent not allowed in this version");
        }

        if (exponent == 0) {
            return (mod == 1) ? 0L : 1L;
        }

        if (number == 1L) {
            return (mod == 1) ? 0L : 1L;
        }

        if (number == 0L) {
            return 0;
        }

        if (number == -1L && ((exponent & 1) != 0)) {
            return (mod == 1) ? 0 : 1;
        }

//        boolean invertResult;
//        if ((invertResult = (exponent < 0))) {
//            exponent = -exponent;
//        }

        long base = (number < 0 || number >= mod) ? number % mod : number;
        if (base < 0) {
            base += mod;
        }
        long result = 1L;
        //long temp = exponent;
        while (exponent > 0) {
            if ((exponent & 1) != 0) {
                result = (result * base) % mod;
            }
            base = (base * base) % mod;
            exponent >>= 1;
        }

        //return (invertResult ? (long) Math.pow(result, -1) % mod : result);
        return result;
    }

    public static long modInverse(long a, long m) {
        long m0 = m;
        long y = 0;
        long x = 1;

        if (m == 1) {
            return 0;
        }

        while (a > 1) {
            long q = a / m;
            long t = m;
            m = a % m;
            a = t;
            t = y;
            y = x - q * y;
            x = t;
        }

        if (x < 0) {
            x += m0;
        }

        return x;
    }

    public static long gcd(long a, long b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

}
