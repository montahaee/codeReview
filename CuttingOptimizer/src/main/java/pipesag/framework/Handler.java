package pipesag.framework;

/**
 *The {@code Handler} Interface should be implemented by any classes whose instances
 * are handled to finde a result for the problem as input 'in' and finally transfer
 * it to a new object. The class must define a method of one argument called handle.
 *<p>
 * The interface is designed to provide a common protocol for objects that wish to
 * take an object that represents their issue, work on a solution and transform it
 * into a new object that represents their solution.
 * It has a Data class that is used to store the Problem and Solution together.
 * @param <I> The type of Problem to work with
 * @param <O> The type of resolve to output
 *
 *
 */
public interface Handler<I,O> {

    /**
     * The class is designed to provide a data container for the problem and solution objects.
     * @param <I> The object class that represents the input.
     * @param <O> The object class that represents the output as a result.
     */
    class Data<I, O> {

        public I in;
        public O out;
    }

    /**
     *Takes an issue, performs the transformation in the form of {@code solve} and returns the output.
     * @param issue - the specified issue that has to be solved.
     * @return the solution object
     */
    O solve(I issue);
}
