package pipesag.utility;

/**
 * Utility class for performing mathematical operations with specific precision and constraints.
 *
 * <p>This class provides methods for comparing floating-point numbers with tolerance (epsilon), rounding values, and checking bounds.</p>
 */
public class CuttingMath {

    /**
     * Compares two double values to determine if they are approximately equal within a specified tolerance (epsilon).
     *
     * @param a       the first double value
     * @param b       the second double value
     * @param epsilon the allowable difference between the two values for them to be considered equal
     * @return {@code true} if the absolute difference between {@code a} and {@code b} is less than or equal to {@code epsilon};
     *         {@code false} otherwise
     */
    public static boolean almostEqual(double a, double b, double epsilon){
        return Math.abs(a - b) <= epsilon;
    }

    /**
     * Compares two double values to determine if they are approximately equal within a default tolerance of 0.0001.
     *
     * @param a the first double value
     * @param b the second double value
     * @return {@code true} if the absolute difference between {@code a} and {@code b} is less than or equal to 0.0001;
     *         {@code false} otherwise
     */
    public static boolean almostEqual(double a, double b){
        return almostEqual(a, b, 0.00000001);
    }

    /**
     * Rounds a double value to two decimal places.
     *
     * @param value the double value to be rounded
     * @return the rounded double value, to two decimal places
     */
    public static double round(double value) {
        return Math.round(value * 100000) / 100000.0;
    }

    /**
     * Checks if a double value is within a specified absolute bound.
     *
     * @param value the double value to be checked
     * @param bound the maximum allowable absolute value
     * @return {@code true} if the absolute value of {@code value} is less than {@code bound};
     *         {@code false} otherwise
     */
    public static boolean isBounded(double value, double bound) {
        return Math.abs(value) < bound;
    }
}
