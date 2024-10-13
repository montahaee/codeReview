package pipesag.io;

import pipesag.datastructure.Cutting;

/**
 * Represents a generator that processes the given data and produces an output.
 * Implementations of this interface define how the output is generated (e.g., writing to a file).
 *
 * @see FileOutputGenerator
 */
public interface OutputGenerator {

    /**
     * Generates an output based on the provided {@link Cutting} data.
     *
     * @param cutting the data to be processed and used for generating the output.
     */
    public void generate(Cutting cutting);
}
