package pipesag.exceptions;

/**
 * This exception is thrown when a file or path cannot be accessed.
 *
 * <p>The {@code FileAccessException} is a specific type of {@code Exception} used in cases where file-related
 * operations fail, such as when a file cannot be read, written, or found. It provides detailed error messages
 * and optionally includes the underlying cause of the exception.
 *
 * <p>This exception can be utilized in file-handling operations to provide meaningful feedback on
 * why a file operation could not succeed.
 *
 * @see Exception
 */
public class FileAccessException extends Exception {

    /**
     * FileAccessException thrown when a path cannot be accessed.
     */
    public FileAccessException() {}

    /**
     * Constructs a FileAccessException with specified detail massage.
     * @param message Message specifying. What caused the exceptions.
     */
    public FileAccessException(String message) {
        super(message);
    }

    /**
     * Constructs a FileAccessException with specified detail massage and cause.
     * @param message Message specifying. What caused the exceptions.
     * @param innerException specifies the cause of the exceptions.
     */
    public FileAccessException(String message, Exception innerException) {
        super(message, innerException);
    }

    /**
     *
     * @return error's massage
     */
    public String getMassage() {
        return super.getMessage();
    }
}
