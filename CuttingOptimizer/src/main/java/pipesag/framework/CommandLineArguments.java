package pipesag.framework;

/**
 * {@code CommandLineArguments} objects contain arguments that were parsed from the commandline.
 * The arguments specify a path to a file or directory.
 *
 * @see CommandLineArgumentsParser
 */
public class CommandLineArguments {

    private String sourceFile;

    /**
     * Constructor takes a string that is the path to a file or directory.
     * @param sourceFile should be a path to file or directory.
     */
    public CommandLineArguments(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * @return Returns the path to file or directory.
     */
    public String getSourceFile() {
        return sourceFile;
    }

    /**
     * Sets the path to file or directory.
     * @param sourceFile Should be absolute or relative path.
     */
    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }
}
