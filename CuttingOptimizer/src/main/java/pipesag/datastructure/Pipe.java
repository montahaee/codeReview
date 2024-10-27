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

    /**
     * Compares this Pipe object with another object for equality.
     *<p>
     * This method performs the following checks:
     * <ol>
     *   <li>If the provided object (`obj`) is the same reference as this Pipe instance,
     *       it returns true immediately, as they are inherently equal.</li>
     *   <li>If the provided object is an instance of Pipe (and not null), it proceeds
     *       to compare their lengths. Equality is determined by checking if the `length`
     *       of this Pipe is identical to the length of the other Pipe.</li>
     *   <li>If the provided object is not a Pipe instance, it returns false.</li>
     * </ol>
     *
     *<P>
     * This implementation leverages Java's `instanceof` operator, which handles null
     * checks automatically.
     * If `obj` is null, `obj instanceof Pipe` will return false,
     * allowing the method to return false without an explicit null check.
     *
     * @param obj the object to compare with this Pipe instance
     * @return true if `obj` is a Pipe with the same `length` as this Pipe;
     *         otherwise, false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Pipe pipe) {
            return length == pipe.getLength();
        }
        return false;
    }
}
