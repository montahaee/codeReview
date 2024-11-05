package pipesag.io;

import pipesag.datastructure.Order;
import pipesag.exceptions.FileAccessException;
import pipesag.exceptions.IncorrectDataFormatException;
import pipesag.framework.Procedure;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class OrderProcedure implements Procedure <Order>{

    private static final Logger LOGGER = Logger.getLogger(OrderProcedure.class.getName());

//    private final File file;
    private final Path resourcePath;
    private final List<Path> filepaths;

    public OrderProcedure(String sourcePath) throws FileAccessException {
        this.resourcePath = Path.of(sourcePath);
        this.filepaths = this.getPaths();
    }

    private List<Path> getPaths() throws FileAccessException{
        List<Path> paths = new ArrayList<>();
        if (!Files.exists(resourcePath)) {
            throw new FileAccessException("Filepath does not exist.");
        }

        if(Files.isDirectory(resourcePath)){
            try(Stream<Path> fileTree = Files.walk(resourcePath, 1)){

                fileTree.filter(Files::isRegularFile).forEach(p -> {
                    String fileName = p.getFileName().toString().toLowerCase();
                    if(!fileName.startsWith("optimized_") && !paths.contains(p) ){
                        paths.add(p);
                    }

                });
            }catch (IOException e){
                throw new FileAccessException("Could not walk file tree.", e);
            }
        }else if (Files.isRegularFile(resourcePath)){
            paths.add(resourcePath);
        }

        return paths;
    }


//    public OrderProcedure(String filepath) throws FileAccessException {
//        try {
//            final String absPath = Paths.get(filepath).toAbsolutePath().toString();
//            file = new File(absPath);
//        } catch (Exception e) {
//            throw new FileAccessException("Could not open file: " + filepath, e);
//        }
//    }


    /**
     * Reads data and produces specified element.
     * <p>
     *
     * @throws IncorrectDataFormatException as Data shouldn't be formatted correctly in order to location.
     */
    @Override
    public Order read() throws IncorrectDataFormatException {
        return null;
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
    public List<String> getLines() throws IncorrectDataFormatException {
        List<String> lines = new ArrayList<>();
        try {
            LOGGER.info("Reading file \"{0}\": " + resourcePath.toAbsolutePath());
            FileReader fileReader = new FileReader(resourcePath.toFile());
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
                LOGGER.log(Level.SEVERE, "Error: File {0} not found", resourcePath.toAbsolutePath());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IOException when reading File: {0}", resourcePath.toAbsolutePath());
            System.err.println(e.getMessage());
        }
        return lines;
    }

    /**
     * Retrieves the absolute file if it exists.
     * @return the absolute {@code file} if it exists; {@code null} otherwise
     */
    public File getFile() {
        return Files.exists(resourcePath) ? resourcePath.toFile() : null;
    }

}
