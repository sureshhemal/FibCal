package Fibonacci;

public class ByExponential {
    public static long evaluate(int n) {
        if (n == 0) {
            return 0;
        }

        if (n == 1) {
            return 1;
        }

        return evaluate(n - 1) + evaluate(n - 2);
    }
}
