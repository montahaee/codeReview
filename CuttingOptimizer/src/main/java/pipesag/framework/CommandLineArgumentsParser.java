package pipesag.framework;

import pipesag.exceptions.InvalidCommandLineArgumentsException;

/**
 * {@code CommandLineArgumentsParser} designed to location arguments that were provided by the command line and return
 * them to the user.The class defines a method of one argument called prompt.
 * */
public class CommandLineArgumentsParser {


    /**
     * Takes an array of arguments and checks it for validity. Returns CommandLineArguments if valid.
     *
     * @param args The input array of arguments should be of length 1.
     * @return {@link CommandLineArguments} object
     * @throws InvalidCommandLineArgumentsException as a user should not input more than one argument.
     */
    public static  CommandLineArguments parse(String[] args) throws InvalidCommandLineArgumentsException {
        if (args.length != 1) {
            String ex = "Invalid numbers of arguments. Expected: 1 or 2. Received: " + args.length;
            throw new InvalidCommandLineArgumentsException(ex);
        }
        return new CommandLineArguments(args[0]);
    }
}
