import Fibonacci.ByExponential;
import Fibonacci.ByPolynomial;
import Fibonacci.FibResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.IntFunction;

public class Main {

    private static final double THRESHOLD_MS = 10.0;

    record FibResultCache(int n, FibResult exponential, FibResult polynomial) {
    }

    public static void main(String[] args) {
        int[] samples = getRandom10NumbersForFindFibonacci();

        List<FibResultCache> cache = new ArrayList<>();

        for (int n : samples) {
            FibResult exponential = byExponential(n);
            FibResult polynomial = byPolynomial(n);

            cache.add(new FibResultCache(n, exponential, polynomial));
        }

        printTable(cache);

        // For n = 39, exponential algorithm took 197ms while polynomial algorithm took 0.006ms.
        // So, for maximum possible n calculation less than 10ms is for polynomial algorithm
        // way bigger than exponential algorithm.
        // So, we can check the exponential algorithm step by step.
        // But that does not work for polynomial algorithm, I guess.
        // For polynomial algorithm, I am going to check it with doubling n.
        int largestNumberForExponentialAlgorithm = checkExponentialAlgorithm();
        int largestNumberForPolynomialAlgorithm = checkPolynomialAlgorithm();

        System.out.printf("Largest number for exponential algorithm: %,d%n", largestNumberForExponentialAlgorithm);
        System.out.printf("Largest number for polynomial algorithm: %,d%n", largestNumberForPolynomialAlgorithm);
    }

    private static int[] getRandom10NumbersForFindFibonacci() {
        return new Random().ints(10, 1, 40)
                .distinct()
                .limit(10)
                .sorted()
                .toArray();
    }

    private static FibResult byPolynomial(int n) {
        long start = System.nanoTime();
        long fib = ByPolynomial.evaluate(n);
        long timeTaken = System.nanoTime() - start;

        return new FibResult(n, fib, timeTaken);
    }

    private static FibResult byExponential(int n) {
        long start = System.nanoTime();
        long fib = ByExponential.evaluate(n);
        long timeTaken = System.nanoTime() - start;

        return new FibResult(n, fib, timeTaken);
    }

    private static void printTable(List<FibResultCache> cache) {
        String border = "+-------+------------------------+------------------------+";

        System.out.println(border);
        System.out.printf("| %5s | %22s | %22s |%n",
                "n", "Exponential Algorithm", "Polynomial Algorithm");
        System.out.println(border);

        for (FibResultCache row : cache) {
            System.out.printf("| %5d | %19.6f ms | %19.6f ms |%n",
                    row.n(),
                    row.exponential().millis(),
                    row.polynomial().millis());
        }

        System.out.println(border);
    }

    private static int checkExponentialAlgorithm() {
        int largestUnder = 0;
        int n = 1;
        double timeTaken;

        do {
            timeTaken = bestOfThree(Main::byExponential, n);

            if (timeTaken < THRESHOLD_MS) {
                largestUnder = n;
            }

            n++;
        } while (timeTaken < THRESHOLD_MS);

        return largestUnder;
    }

    private static int checkPolynomialAlgorithm() {
        int largestUnder = 0;
        int n = 1;
        double timeTaken;

        do {
            timeTaken = bestOfThree(Main::byPolynomial, n);

            if (timeTaken < THRESHOLD_MS) {
                largestUnder = n;
            }

            n = n * 2;
        } while (timeTaken < THRESHOLD_MS);

        // The answer is somewhere between the last power of two and the twice of that, (i.e. twice of largestUnder)
        // Now I'm going to search within that range,
        int low = largestUnder;
        int high = largestUnder * 2;

        while (low + 1 < high) { // because of intiger division, it is possible that mid = low. So, we take low + 1
            int mid = low + (high - low) / 2;

            double midTime = bestOfThree(Main::byPolynomial, mid);

            if (midTime < THRESHOLD_MS) {
                low = mid;      // mid passed, so the answer is at or above it
            } else {
                high = mid;     // mid failed, so the answer is below it
            }
        }

        return low;
    }

    /**
     * Based on running machine current performance,
     * time taken can be varied. So I'm gonna try three times and
     * say the lowest time is the time for given 'n'
     */
    private static double bestOfThree(IntFunction<FibResult> algorithm, int n) {
        double best = Double.MAX_VALUE;
        for (int attempt = 0; attempt < 3; attempt++) {
            best = Math.min(best, algorithm.apply(n).millis());
        }
        return best;
    }
}
