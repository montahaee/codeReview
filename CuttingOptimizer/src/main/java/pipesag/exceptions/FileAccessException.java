package pipesag.exceptions;

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
