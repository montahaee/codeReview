package pipesag.io;

import pipesag.exceptions.FileAccessException;
import pipesag.exceptions.IncorrectDataFormatException;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleFileInputReader implements InputReader {

    private static final Logger LOGGER = Logger.getLogger(SimpleFileInputReader.class.getName());

    private final File file;

    public SimpleFileInputReader(String filepath) throws FileAccessException {
        try {
            final String absPath = Paths.get(filepath).toAbsolutePath().toString();
            file = new File(absPath);
        } catch (Exception e) {
            throw new FileAccessException("Could not open file: " + filepath, e);
        }
    }

    /**
     * Reads the contents of a file line by line and returns a list of strings, where each string represents a line from the file.
     *
     * <p>This method opens the file, reads each line using a {@link BufferedReader}, and adds the lines to a {@link List}.
     * If the file is empty or if an error occurs during the reading process, appropriate exceptions are thrown or logged.</p>
     *
     * @return a {@link List} of strings, where each string represents one line from the file. If the file is empty,
     *         the list will also be empty.
     *
     * @throws IncorrectDataFormatException if the file contains no data (i.e., the first line is null).
     *
     * @see FileReader
     * @see BufferedReader
     * @see java.util.List
     * @see java.util.ArrayList
     */
    @Override
    public List<String> getLines() throws IncorrectDataFormatException {
        List<String> lines = new ArrayList<>();
        try {
            LOGGER.info("Reading file \"{0}\": " + file.getAbsolutePath());
            FileReader fileReader = new FileReader(file);
            String line;

            try (BufferedReader reader = new BufferedReader(fileReader)) {
                line = reader.readLine();
                if (line == null) {
                    throw new IncorrectDataFormatException("input data is empty!");
                }
                while (line != null) {
                    lines.add(line);
                    line = reader.readLine();
                }
            } catch (FileNotFoundException e) {
                LOGGER.log(Level.SEVERE, "Error: File {0} not found", file.getAbsolutePath());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IOException when reading File: {0}", file.getAbsolutePath());
            System.err.println(e.getMessage());
        }
        return lines;
    }

    /**
     * Retrieves the absolute file if it exists.
     * @return the absolute {@code file} if it exists; {@code null} otherwise
     */
    public File getFile() {
        return file.exists() ? file.getAbsoluteFile() : null;
    }
}
