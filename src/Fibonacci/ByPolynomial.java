package Fibonacci;

public class ByPolynomial {
    public static long evaluate(int n) {
        if (n == 0) {
            return 0;
        }

        long[] fibSequence = new long[n + 1];

        fibSequence[0] = 0;
        fibSequence[1] = 1;

        for (int i = 2; i <= n; i++) {
            fibSequence[i] = fibSequence[i - 1] + fibSequence[i - 2];
        }

        return fibSequence[n];
    }
}
