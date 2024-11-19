package pipesag.framework;

import pipesag.exceptions.FileAccessException;

/**
 * The {@code Consumer} interface should be implemented by any classes that will have to receive data
 * and write it somewhere. The class must define a method with one argument called write.<br>
 *
 * The Interface is designed to provide a common protocol for instances that are designed to be consumers.
 *
 * @param <D> The Object that has to be consumed.
 */
public interface Consumable<D> {

    /**
     * Takes data and writes it to the specified location.
     * @param data the data that has to be transferred.
     * @throws FileAccessException as filepath could be incorrect.
     */
    void write(D data) throws FileAccessException;
}
