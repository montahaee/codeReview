package pipesag.exceptions;

/**
 * This exception is thrown to indicate that there is an issue with the provided command-line arguments.
 *
 * <p>This exception can be used to signal various errors such as invalid format, missing required arguments,
 * or unsupported values when parsing or processing command-line input.
 *
 * <p>It extends the {@code Exception} class, allowing it to be used as a checked exception in methods that
 * handle command-line arguments.
 *
 * @see Exception
 */
public class InvalidCommandLineArgumentsException extends Exception {

    /**
     * Constructs a default InvalidCommandLineArgumentsException.
     */
    public InvalidCommandLineArgumentsException() {}

    /**
     * Constructs an InvalidCommandLineArgumentsException with the specified detail message.
     * @param message specified detail message.
     */
    public InvalidCommandLineArgumentsException(String message) {super(message);}

    /**
     * Constructs an InvalidCommandLineArgumentsException with the specified detail message and cause.
     * @param message specified detail message.
     * @param cause as the reason of the exceptions.
     */
    public InvalidCommandLineArgumentsException(String message, Throwable cause) {super(message, cause);}

    /**
     * Constructs an InvalidCommandLineArgumentsException with the specified cause.
     * @param cause reason for the error.
     */
    public InvalidCommandLineArgumentsException(Throwable cause) {super(cause);}
}
