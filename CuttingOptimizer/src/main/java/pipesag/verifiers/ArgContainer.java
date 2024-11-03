package pipesag.verifiers;

import pipesag.datastructure.Order;
import pipesag.exceptions.InvalidCommandLineArgumentsException;

import java.io.File;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code ArgContainer} class is responsible for managing command-line arguments
 * for a file processing application. It verifies and stores the input and output
 * file paths specified by the user through command-line arguments.
 *
 * <p>It contains an inner class {@code ArgsVerifier} that handles the logic for
 * validating and parsing the command-line arguments. The class also uses an
 * enumeration {@code ArgKey} to represent valid argument keys.
 *
 * <p>Instances of this class should be created by passing command-line arguments
 * for verification and extraction of input/output file paths.
 *
 * @see ArgsVerifier
 * @see ArgKey
 */
public class ArgContainer {

    private static final Logger LOGGER = Logger.getLogger(ArgContainer.class.getName());

    private File inputFile;
    private File outputFile;

    // Private constructor to prevent direct instantiation.
    private ArgContainer () {

    }

    /**
     * The {@code ArgsVerifier} inner class is responsible for verifying and
     * validating command-line arguments. It provides methods to parse arguments
     * and return an instance of {@code ArgContainer} with the verified paths.
     *
     * <p>It also contains constants to define readable argument formats and handles
     * any exceptions that may occur during the verification process.
     */
    public static class ArgsVerifier {

        private static final Logger LOG = Logger.getLogger(ArgsVerifier.class.getName());

        static final String READABLE_ARG_EXAMPLE = "Arguments must follow the format" +
                " \"argumentName:argumentValue\". Example: \"inputFile:example.txt\" - Case-sensitive.";

        /**
         * Verifies the command-line arguments provided and returns an
         * {@code ArgContainer} instance containing the valid input and output file paths.
         *
         * @param args the command-line arguments to verify.
         * @return an {@code ArgContainer} containing the verified file paths.
         * @throws InvalidCommandLineArgumentsException if any argument is invalid.
         */
        public ArgContainer verifyAndGet(String[] args) throws InvalidCommandLineArgumentsException {
            ArgContainer argContainer = new ArgContainer();

            for (String arg : args) {
                if (arg == null || arg.isEmpty()) {
                    continue;
                }
                String[] kvPair = arg.split(":");
                if (kvPair.length != 2) {
                    LOG.log(Level.SEVERE, "Argument \"{0}\" not readable! It will be ignored! {1}", new Object[]{ arg, READABLE_ARG_EXAMPLE});
                } else {
                    argContainer.add(kvPair[0].trim(), kvPair[1].trim());
                }
            }
            assert (argContainer.argsValid());
            return argContainer;
        }

        /**
         * Verifies and adjusts the provided {@code Order} object if necessary.
         *
         * @param order the {@code Order} object to verify and adjust.
         */
        public void verifyAndAdjustIfNecessary(Order order) {
            // Implementation for verifying and adjusting the order if needed.
        }
    }

    /**
     * Enumeration of valid argument keys for the command-line arguments.
     * Each key corresponds to a specific type of argument expected by
     * the {@code ArgContainer} class.
     */
    public enum ArgKey {
        INPUT_FILE("inputFile"),
        OUTPUT_FILE("outputFile");

        private final String key;

        private ArgKey(String key) {
            this.key = key;
        }

       /**
         * Returns the {@code ArgKey} corresponding to the given key string.
         *
         * @param key the key string to match against the enumeration values.
         * @return the matching {@code ArgKey}, or {@code null} if no match is found.
         */
        public static ArgKey fromKey(String key) {
            for (ArgKey argKey : ArgKey.values()) {
                if (argKey.getKey().equals(key)) {
                    return argKey;
                }
            }
            return null;
        }

        /**
         * Retrieves the string representation of the argument key.
         *
         * @return the key as a {@code String}.
         */
        public String getKey() {
            return key;
        }
    }

    /**
     * Retrieves the input file associated with this {@code ArgContainer}.
     *
     * @return the input file as a {@code File} object.
     */
    public File getInputFile() {
        return inputFile;
    }

    /**
     * Retrieves the output file associated with this {@code ArgContainer}.
     *
     * @return the output file as a {@code File} object.
     */
    public File getOutputFile() {
        return outputFile;
    }

    /**
     * Adds a key-value pair to this {@code ArgContainer} based on the provided
     * argument key and value. Validates the key and attempts to fill
     * the corresponding input or output file.
     *
     * @param key the argument key as a {@code String}.
     * @param value the value associated with the key as a {@code String}.
     * @throws InvalidCommandLineArgumentsException if the key or value is invalid.
     */
    public void add(String key, String value) throws InvalidCommandLineArgumentsException {
        ArgKey argKey = ArgKey.fromKey(key);
        if (argKey == null) {
            LOGGER.log(Level.SEVERE, "Unknown argument \"{0}\" - it will be ignored!", key);
            return;
        }
        switch (argKey) {
            case INPUT_FILE: fillInputFile(value); break;
            case OUTPUT_FILE: fillOutputFile(value); break;
        }
    }

    /**
     * Validates the current state of the arguments in this {@code ArgContainer}.
     *
     * @return {@code true} if the arguments are valid; {@code false} otherwise.
     */
    public boolean argsValid() {
        if (inputFile == null) {
            LOGGER.log(Level.SEVERE, "Please specify argument \"{0}\"! {1}", new Object[]{ArgKey.INPUT_FILE.getKey(), ArgsVerifier.READABLE_ARG_EXAMPLE});
            return false;
        }
        return true;
    }

    /**
     * Fills the output file field with the specified value, creating
     * a {@code File} object and validating it.
     *
     * @param value the path to the output file as a {@code String}.
     * @throws InvalidCommandLineArgumentsException if the file cannot be created.
     */
    private void fillOutputFile(String value) throws InvalidCommandLineArgumentsException {
        try {
            this.outputFile = Paths.get(value).toAbsolutePath().toFile();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not create output file: \"{0}\"", value);
            throw new InvalidCommandLineArgumentsException(e.getMessage());
        }
    }

    /**
     * Fills the input file field with the specified value, creating
     * a {@code File} object and validating it.
     *
     * @param value the path to the input file as a {@code String}.
     * @throws InvalidCommandLineArgumentsException if the file cannot be read.
     */
    private void fillInputFile(String value) throws InvalidCommandLineArgumentsException {
        try {
            this.inputFile = Paths.get(value).toAbsolutePath().toFile();
            assert (inputFile.isFile());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not read file \"{0}\"!", value);
            throw new InvalidCommandLineArgumentsException(e.getMessage());
        }
    }

}
