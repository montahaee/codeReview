package pipesag.datastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a warehouse that stores a collection of pipes.
 *
 * @see Pipe
 */
public class Warehouse {
    private final List<Pipe> pipeList;

    /**
     * Creates a warehouse with a predefined list of pipes.
     */
    public Warehouse() {
        pipeList = new ArrayList<>();
        pipeList.add(new Pipe(2));
        pipeList.add(new Pipe(3));
        pipeList.add(new Pipe(4));
        pipeList.add(new Pipe(5));
    }

    /**
     * Returns an unmodifiable list of pipes available in the warehouse.
     *
     * @return the list of pipes.
     * @see Collections#unmodifiableList(List)
     * @see Pipe
     */
    public List<Pipe> getPipeList() {
        return Collections.unmodifiableList(pipeList);
    }
}
