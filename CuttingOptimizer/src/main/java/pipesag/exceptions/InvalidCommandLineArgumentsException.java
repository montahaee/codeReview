package pipesag.exceptions;

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
