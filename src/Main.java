import Fibonacci.ByExponential;
import Fibonacci.ByPolynomial;
import Fibonacci.FibResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

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
}
