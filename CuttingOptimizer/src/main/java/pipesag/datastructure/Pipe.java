package pipesag.datastructure;

/**
 * Represents a pipe with a specified length.
 * The length of the pipe must be greater than zero.
 * {@link IllegalArgumentException} if the length is less than or equal to 0.
 *
 * @see Warehouse
 */
public class Pipe {

    private final double length;

    /**
     * Constructor for creating a pipe with a specific length.
     *
     * @param length the length of the pipe must be greater than 0
     * @throws IllegalArgumentException if {@code length <= 0}.
     */
    public Pipe(double length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }
        this.length = length;
    }

    /**
     * Copy constructor for creating a new Pipe instance from an existing Pipe object.
     *
     * @param pipe the Pipe to copy from
     */
    public Pipe(Pipe pipe) {
        this(pipe.getLength());
    }

    /**
     *
     * @return the length of the pipe
     */
    public double getLength() {
        return length;
    }
}
