package math;

public class MathFunctions {
    public static long leastCommonMultiple(long a, long b) {
        return Math.abs(a * b) / greatestCommonDivisor(a, b);
    }

    public static long greatestCommonDivisor(long a, long b) {
        if (b == 0L) {
            return a;
        }

        return greatestCommonDivisor(b, a % b);
    }
}
