package Fibonacci;

public record FibResult(int n, long fib, long timeTaken) {

    /** The elapsed time expressed in milliseconds. */
    public double millis() {
        return timeTaken / 1_000_000.0;
    }
}
