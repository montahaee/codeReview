package pipesag.io;

import org.jetbrains.annotations.NotNull;
import pipesag.datastructure.Customer;
import pipesag.datastructure.Order;
import pipesag.datastructure.Pipe;
import pipesag.exceptions.FileAccessException;
import pipesag.exceptions.IncorrectDataFormatException;
import pipesag.framework.Procedure;
import pipesag.utility.FilenameConfig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class OrderProcedure implements Procedure <OrderJob>, Runnable {

    private static final Logger LOGGER = Logger.getLogger(OrderProcedure.class.getName());

    private Path resourcePath;
    private final DataStream<OrderJob> jobDataStream;
    private final List<Path> filepaths;
    private final FilenameConfig filenameConfig;



    public OrderProcedure(String sourcePath, DataStream<OrderJob> jobDataStream) throws FileAccessException {
        this.resourcePath = Path.of(sourcePath);
        this.jobDataStream = jobDataStream;
        this.filenameConfig = new FilenameConfig("optimized");
        this.filepaths = this.getPaths();
    }

    private List<Path> getPaths() throws FileAccessException{
        List<Path> paths = new ArrayList<>();
        if (!Files.exists(resourcePath)) {
            throw new FileAccessException("Filepath does not exist.");
        }

        if(Files.isDirectory(resourcePath)){
            try(Stream<Path> fileTree = Files.walk(resourcePath, 1)){

                fileTree.filter(Files::isRegularFile).forEach(p -> {
                    String fileName = p.getFileName().toString().toLowerCase();
                    if(!fileName.startsWith("optimized_") && !paths.contains(p) ){
                        paths.add(p);
                    }

                });
            }catch (IOException e){
                throw new FileAccessException("Could not walk file tree.", e);
            }
        }else if (Files.isRegularFile(resourcePath)){
            paths.add(resourcePath);
        }

        return paths;
    }

    /**
     * Reads data and produces specified element.
     * <p>
     *
     * @throws IncorrectDataFormatException as Data shouldn't be formatted correctly in order to location.
     */
    @Override
    public OrderJob read() throws IncorrectDataFormatException {
        List<String> lines = this.getLines();
        Map.Entry<Order, String> order = this.job(lines);
        OrderJob job = new OrderJob(resourcePath.toString(), order.getKey());
        job.setError(order.getValue());
        return job;
    }

    /**
     * Reads the contents of a file line by line and returns a list of strings, where each string represents a line from the file.
     *
     * <p>This method opens the file, reads each line using a {@link BufferedReader}, and adds the lines to a {@link List}.
     * If the file is empty or if an error occurs during the reading process, appropriate exceptions are thrown or logged.</p>
     *
     * @return a {@link List} of strings, where each string represents one line from the file. If the file is empty,
     *         the list will also be empty.
     *
     * @throws IncorrectDataFormatException if the file contains no data (i.e., the first line is null).
     *
     * @see FileReader
     * @see BufferedReader
     * @see java.util.List
     * @see java.util.ArrayList
     */
    private List<String> getLines() throws IncorrectDataFormatException {
        List<String> lines = new ArrayList<>();
        try {
            LOGGER.info("Reading file \"{0}\": " + resourcePath.toAbsolutePath());
            FileReader fileReader = new FileReader(resourcePath.toFile());
            String line;

            try (BufferedReader reader = new BufferedReader(fileReader)) {
                line = reader.readLine();
                if (line == null) {
                    throw new IncorrectDataFormatException("input data is empty!");
                }
                while (line != null) {
                    lines.add(line);
                    line = reader.readLine();
                }
            } catch (FileNotFoundException e) {
                LOGGER.log(Level.SEVERE, "Error: File {0} not found", resourcePath.toAbsolutePath());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IOException when reading File: {0}", resourcePath.toAbsolutePath());
            System.err.println(e.getMessage());
        }
        return lines;
    }

    /**
     * Processes a list of lines representing an order, parses the data to
     * extract customer details and ordered items, and creates an {@code Order}
     * object with the information found.
     *
     * @param lines a {@code List} of {@code String} representing the order details
     * @return an {@code Order} object if successfully parsed; {@code null} if no valid order is found
     */
    private Map.Entry<Order, String>  job(@NotNull List <String> lines) {
        boolean notIdentified = true;
        Customer customer = new Customer();
        Order order = new Order();
        final Pattern alphabeticPattern = Pattern.compile("^[a-zA-Z]+$");
        final Pattern integerPattern = Pattern.compile("^[0-9]+$"); // or "\\d+"
        final Pattern doublePattern = Pattern.compile("^[0-9]+\\.?[0-9]+$");
        String[] lineContents;
        String massage ="";
        // If no lines are provided, print a message and return null
        if (lines.isEmpty()) {
            massage = "No orders found";
            return Map.entry(order, massage);
        }

        // Process each line to identify customer and order item information
        for (String line : lines) {
            if (line.isEmpty() || line.startsWith("#") || (line.startsWith("\"") && line.endsWith("\""))) {
                massage = "no information to read found";
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
        return Map.entry(order, massage); // Return the created order object
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

    /**
     * Retrieves the absolute file if it exists.
     * @return the absolute {@code file} if it exists; {@code null} otherwise
     */
    public File getFile() {
        return Files.exists(resourcePath) ? resourcePath.toFile() : null;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general process of the method {@code run} is that it may
     * take any action whatsoever.
     * <p>
     * Hir therefor the list of files will be iterated  and invoked the
     * {@link #read()} method. Continues until all input files have
     * been processed, meanwhile, each processed file is transferred to
     * the DataStream.
     * @see Thread#run()
     * @see DataStream
     */
    @Override
    public void run() {
        Set<Path> processedFiles = new HashSet<>();
        String fileSeparator = File.separator;
        while (processedFiles.size() < filepaths.size()) {
            filepaths.forEach(path -> {
                if (filenameConfig.isFileExits(path)) {
                    processedFiles.add(path);
                    return; // Exits this lambda iteration, similar to `continue`
                }
                try {
                    resourcePath = path;
                    this.jobDataStream.add(read());
                } catch (IncorrectDataFormatException e) {
                    System.err.println(e.getMessage());
                    Thread.currentThread().interrupt();
                }
            });
        }
        this.jobDataStream.add(new OrderJob());
    }
}
