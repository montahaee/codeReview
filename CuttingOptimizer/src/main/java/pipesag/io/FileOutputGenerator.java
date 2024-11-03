package pipesag.io;

import pipesag.datastructure.Cutting;
import pipesag.datastructure.Processing;
import pipesag.exceptions.FileAccessException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of {@link OutputGenerator} that writes the processing results to a specified file.
 *<p>
 * This class handles file access and writes the processed data to a file using a {@link PrintWriter}.
 *
 * @see OutputGenerator
 */
public class FileOutputGenerator implements OutputGenerator {

    private static final Logger LOGGER = Logger.getLogger(FileOutputGenerator.class.getName());

    private final Path filePath;

    /**
     * Constructs a {@code FileOutputGenerator} with the specified output file path.
     *
     * @param outputFilePath the path to the output file where the results will be written
     * @throws FileAccessException if the output path is invalid or inaccessible
     */
    public FileOutputGenerator(String outputFilePath) throws FileAccessException {
        try {
            filePath = Paths.get(outputFilePath).toAbsolutePath();
        } catch (NullPointerException e) {
            throw new FileAccessException("Output data not readable!", e);
        }
    }

    /**
     * Generates an output by writing the {@link Processing} data to the specified file.
     *
     * @param process the data to be written to the output file
     */
    @Override
    public void generate(Processing process) {
        LOGGER.log(Level.INFO, "Writing result to file \"{0}\"", filePath.toString());
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath.toFile(), true)))) {
            writer.print(process.toString());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing to file \"{0}\"", new Object[]{filePath.toString(), e});
        }
    }
}
