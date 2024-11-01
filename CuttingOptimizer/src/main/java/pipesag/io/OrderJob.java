package pipesag.io;

import org.jetbrains.annotations.NotNull;
import pipesag.datastructure.Customer;
import pipesag.datastructure.Order;
import pipesag.datastructure.Pipe;

import java.util.*;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.regex.Pattern;

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
 * @see SimpleFileInputReader
 */
public class OrderJob {

    private String error;
    private final String filepath;

    /**
     * Default constructor that initializes {@code filepath} with an empty string.
     */
    public OrderJob(){
        this.filepath = "";
    }

    /**
     * Constructs an {@code OrderJob} with the specified file path and initializes
     * the error message as an empty string.
     *
     * @param filepath represents the path of the file from which order data
     *                 is read, typically from {@link SimpleFileInputReader#getFile()}.
     */
    public OrderJob(String filepath) {
        this.filepath = filepath;
        this.error ="";
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
     * Processes a list of lines representing an order, parses the data to
     * extract customer details and ordered items, and creates an {@code Order}
     * object with the information found.
     *
     * @param lines a {@code List} of {@code String} representing the order details
     * @return an {@code Order} object if successfully parsed; {@code null} if no valid order is found
     */
    public Order job(@NotNull List <String> lines) {
        boolean notIdentified = true;
        Customer customer = new Customer();
        Order order = new Order();
        final Pattern alphabeticPattern = Pattern.compile("^[a-zA-Z]+$");
        final Pattern integerPattern = Pattern.compile("^[0-9]+$"); // or "\\d+"
        final Pattern doublePattern = Pattern.compile("^[0-9]+\\.?[0-9]+$");
        String[] lineContents;

        // If no lines are provided, print a message and return null
        if (lines.isEmpty()) {
            error = "No orders found";
            return null;
        }

        // Process each line to identify customer and order item information
        for (String line : lines) {
            if (line.isEmpty() || line.startsWith("#") || (line.startsWith("\"") && line.endsWith("\""))) {
                error = "no information to read found";
                continue;
            }
            if (!line.startsWith("#") && line.contains("#")) {
                line = line.substring(0, line.indexOf("#"));
            }

            // Parse potential customer information lines with commas
            if (line.contains(",") && notIdentified) {
                lineContents = line.trim().split(",");
                if (2 <= lineContents.length && lineContents.length <= 7) {
                    for (int i = 0; i < lineContents.length - 1 && notIdentified; i++) {
//                        notIdentified = alphabeticPattern.matcher(lineContents[i].trim()).find();

                        notIdentified = alphabeticPattern.matcher(lineContents[i].trim()).matches();
                    }
                    String lastElement = lineContents[lineContents.length - 1].trim();
                    boolean isLastElementAlphabetic = alphabeticPattern.matcher(lastElement).matches();
                    boolean isLastElementDigit = integerPattern.matcher(lineContents[lineContents.length - 1].trim()).matches();

                    // Create a customer with name or ID depending on last element type
                    if (notIdentified && (isLastElementAlphabetic || isLastElementDigit)) {
                        String fullname = isLastElementDigit ? String.join(" ", Arrays.copyOf(lineContents, lineContents.length - 1)).trim() :
                                String.join(" ", lineContents).trim();
                        int id = isLastElementAlphabetic ? generateCustomerId(fullname) : Integer.parseInt(lastElement);
                        customer = new Customer(id, fullname);
                        notIdentified = false;
                    }
                }

            } else if (line.contains(";")) { // Parse potential order item information lines with semicolons
                lineContents = line.trim().split(";");
                boolean notItemised = true;
                List<Map.Entry<Pipe, Integer>> items = new ArrayList<>();
                order = new Order(customer);

                // Process each item in the line to create order items
                for (int i = 0; i < lineContents.length && notItemised; i++) {
                    String[] item = lineContents[i].trim().split("\\*");
                    notItemised = item.length == 2;
                    notItemised &= integerPattern.matcher(item[0].trim()).matches();
                    notItemised &= doublePattern.matcher(item[1].trim()).matches();
                    int mount = Integer.parseInt(lineContents[0].trim());
                    double size = Double.parseDouble(lineContents[1].trim());
                    order.addItem(new Pipe(size), mount); // Add item to the order
                }
            }
        }
            return order; // Return the created order object
    }

    /**
     * Generates a unique, non-negative random ID based on the customer's name.
     * <p>This method creates a hash from the trimmed and lowercased name,
     * which is then used as a seed to produce a unique random ID.</p>
     *
     * @param name the customer's full name used to generate a unique ID
     * @return a unique, non-negative random ID as an {@code integer}
     */
    private int generateCustomerId(String name){
        // Create a hash code from the name after trimming and converting to lowercase
        int nameHash = name.trim().toLowerCase().hashCode();

        // Initialize a random generator using "L64X128MixRandom" with the generated hash as the seed
        RandomGenerator randomGenerator = RandomGeneratorFactory.of("L64X128MixRandom").create(nameHash);

        // Generate a random integer and return its absolute value to ensure a unique non-negative ID
        return Math.abs(randomGenerator.nextInt());
    }
}
