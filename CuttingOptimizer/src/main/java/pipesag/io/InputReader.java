package pipesag.io;

import pipesag.exceptions.IncorrectDataFormatException;

import java.util.List;

/**
 * Interface for reading input data as a list of lines.
 *
 * <p>Implementations of this interface are responsible for reading input data from various sources (e.g., files, streams)
 * and returning the data as a list of strings, where each string represents a line of input.</p>
 *
 */
public interface InputReader {

    /**
     * Reads input data and returns it as a list of lines.
     *
     * @return a {@link List} of strings, where each string represents one line of input data
     * @throws IncorrectDataFormatException if the input data format is incorrect or if the input data is invalid
     */
    public List<String> getLines() throws IncorrectDataFormatException;
}
