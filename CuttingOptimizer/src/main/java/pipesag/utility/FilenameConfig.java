package pipesag.utility;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;

/**
 * The {@code FilenameConfig} record is designed to help in generating output
 * filenames based on a specified prefix and checking for the existence of
 * files with generated names in a given directory.
 *
 * <p>This record provides functionality to create output filenames by combining
 * the provided prefix with the original filename (excluding the extension).
 * Additionally, it includes a method to verify if a file with the generated
 * name already exists in the parent directory of the specified path.
 */
public record FilenameConfig (String prefix) {


    /**
     * Generates an output filename based on the provided path by appending
     * the specified prefix to the original filename (without its extension).
     *
     * @param path the original file path from which to derive the output filename
     * @return the generated output filename as a {@code String}
     */
    String generateOutputFilename(String path) {
        int indexOfChild = path.lastIndexOf(File.separator) + 1;
        String filename = path.substring(0, indexOfChild);
        filename += prefix;
        filename += path.substring(indexOfChild, path.indexOf("."));
        return filename;
    }

    /**
     * Checks whether a file with the generated output filename exists
     * in the parent directory of the specified current path.
     *
     * @param currentPath the {@code Path} to check for existing files
     * @return {@code true} if a file with the generated name exists;
     *         {@code false} otherwise
     */
    public boolean isFileExits(@NotNull Path currentPath) {
        File[] listOfFiles = currentPath.getParent().toFile().listFiles();
        for (File file : (listOfFiles != null) ? listOfFiles : new File[0]) {
            if (file.isFile()) {
                String fileName = file.getAbsolutePath().split("\\.(?=[^.])+$")[0];
                String filenameWithoutExtension = generateOutputFilename(currentPath.toString());
                if (fileName.equalsIgnoreCase(filenameWithoutExtension) ||
                        filenameWithoutExtension.equals(fileName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
