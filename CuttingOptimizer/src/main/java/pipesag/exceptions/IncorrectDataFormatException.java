package pipesag.exceptions;

/**
 * IncorrectDataFormatException thrown when the providing data is wrongly formated.
 */
public class IncorrectDataFormatException extends Throwable {

    /**
     * Constructs a default IncorrectDataFormatException.
     */
    public IncorrectDataFormatException() {super();}

    /**
     * Constructs an IncorrectDataFormatException with specified detail message.
     * @param message specified detail the exceptions occurred.
     */
    public IncorrectDataFormatException(String message) {super(message);}

    /**
     * Constructs an IncorrectDataFormatException with specified detail message and cause.
     * @param message specified detail message
     * @param innerException cause of the exceptions.
     */
    public IncorrectDataFormatException(String message, Exception innerException) {super(message, innerException);}

    /**
     *
     * @param msg massage error
     * @param cause the reason of the error
     */
    public IncorrectDataFormatException(String msg, Throwable cause) {super(msg, cause);}
}
