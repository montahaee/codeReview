package pipesag.io;

import org.jetbrains.annotations.NotNull;
import pipesag.datastructure.Customer;
import pipesag.datastructure.Order;
import pipesag.datastructure.Pipe;

import java.util.*;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.regex.Pattern;

// TODO improve the program to get a list of orders!

/**
 * The {@code OrderJob} class is responsible for processing a list of strings
 * representing customer orders and extracting structured information into
 * an {@code Order} object. This includes identifying customer details
 * and order items based on specific patterns in the input data.
 *
 * <p>This class supports creating an {@code Order} instance with relevant
 * details, such as customer information and items ordered, based on patterns
 * in the data lines provided. The class also handles error messages
 * when parsing issues occur.
 * @see OrderProcedure
 */
public class OrderJob {

    private String error;
    private final String filepath;

    private Order order;

    /**
     * Default constructor that initializes {@code filepath} with an empty string.
     */
    public OrderJob(){
        this.filepath = null;
    }

    /**
     * Constructs an {@code OrderJob} with the specified file path and initializes
     * the error message as an empty string.
     *
     * @param filepath represents the path of the file from which order data
     *                 is read, typically from {@link OrderProcedure#getFile()}.
     */
    public OrderJob(String filepath, Order order) {
        this.filepath = filepath;
        this.error ="";
        this.order = order;
    }

    /**
     * Retrieves the error message, if any, generated during the processing
     * of the order data.
     *
     * @return the error message as a {@code String}, or an empty string if no errors occurred.
     */
    public String getError() {
        return error;
    }

    /**
     * Changes the error to the finding current one.
     * @param error represents an eventual error, which is not ignorable.
     */
    public void setError(String error) {
        this.error = error;
    }


    /**
     *
     * @return the new order read from input data.
     */
    public Order getOrder() {
        return this.order;
    }
}
