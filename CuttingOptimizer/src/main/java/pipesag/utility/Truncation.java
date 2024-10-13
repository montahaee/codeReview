package pipesag.utility;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utility class for formatting numbers and strings with specific patterns.
 *
 * <p>The {@code Truncation} class provides static methods to format double values or strings
 * based on given formats and locale. This class cannot be instantiated.</p>
 */
public class Truncation {

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private Truncation(){}

    /**
     * Formats a double value to two decimal places using the English locale.
     *
     * @param d the double value to format
     * @return a formatted string representing the double value with two decimal places
     */
    @Contract(pure = true)
    public static @NotNull String format(double d){
        return new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH)).format(d);
    }

    /**
     * Formats a string by appending a formatted double value with two decimal places.
     * Optionally, adds a new line character after the formatted value.
     *
     * @param comment   the base string to append the formatted number to
     * @param nextLine  whether to add a new line character at the end of the formatted output
     * @param number    the double value to format
     * @return a formatted string containing the base comment and the formatted number
     */
    @Contract(pure = true)
    public static @NotNull String form(String comment, boolean nextLine, double number){
        return String.format(Locale.ENGLISH, comment + "%1.2f" + (nextLine? System.lineSeparator() : ""), number);
    }

    /**
     * Formats a string by appending another string formatted according to a specified format.
     * Optionally, adds a new line character after the formatted value.
     *
     * @param comment   the base string to append the formatted info to
     * @param format    the format pattern for the appended string (e.g., {@code %s}, {@code %d})
     * @param nextLine  whether to add a new line character at the end of the formatted output
     * @param info      the string value to format and append
     * @return a formatted string containing the base comment and the formatted info
     */
    @Contract(pure = true)
    public static @NotNull String form(String comment, String format, boolean nextLine, String info){
        return String.format(Locale.ENGLISH, comment + "%" + format+ (nextLine? System.lineSeparator() : ""), info);
    }
}
