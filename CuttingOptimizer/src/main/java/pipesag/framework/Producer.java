package pipesag.framework;

import pipesag.exceptions.IncorrectDataFormatException;

/**
 * The Producer Interface should be implemented by any classes whose instances
 * are intended to location some kind of input data. The class must define a supplier
 * methode(a methode with no arguments but return something) called read.
 * <p>
 * The Interface is designed to provide a common protocol for instances
 * that are designed to be producers.
 * @param <I> The type of output of {@link #read()} that should be produced
 */
public interface Producer<I>{

    /**
     * Reads data and produces specified element.
     * <p>
     * @throws IncorrectDataFormatException as Data shouldn't be formatted correctly in order to location.
     */
    public I read() throws IncorrectDataFormatException;

}
