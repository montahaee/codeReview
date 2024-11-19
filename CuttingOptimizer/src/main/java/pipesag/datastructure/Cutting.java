package pipesag.datastructure;

import org.jetbrains.annotations.NotNull;
import pipesag.utility.CuttingMath;
import pipesag.utility.Truncation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a cutting process where pieces of pipes
 * are cut from a larger pipe stored in the warehouse.
 *
 * @see Pipe
 * @see Warehouse
 */
public class Cutting {
    private final List<Pipe> cuttingPipes;
    private final Pipe warehousePipe;
    private double offcuts;

    /**
     * Creates a cutting process with a specified warehouse pipe.
     *
     * @param warehousePipe the pipe from which pieces will be cut.
     */
    public Cutting(Pipe warehousePipe) {
        this(warehousePipe, new ArrayList<>());
        updateOffcuts();
    }

    /**
     * Creates a cutting process with a specified warehouse pipe and a list of already cut pieces.
     *
     * @param warehousePipe the pipe from which pieces will be cut.
     * @param cuttingPipes the list of cut pieces.
     */
    public Cutting(Pipe warehousePipe, List<Pipe> cuttingPipes) {
        this.warehousePipe = warehousePipe;
        this.cuttingPipes = new ArrayList<>(cuttingPipes.stream().map(Pipe::new).toList());
        updateOffcuts();
    }

    /**
     * Copy constructor for creating a new cutting process from an existing one.
     *
     * @param other the cutting process to copy.
     */
    public Cutting(Cutting other) {
        this(other.warehousePipe, other.cuttingPipes);
        this.offcuts = other.getOffcuts();
    }

    /**
     * Gets the original pipe used in the cutting process.
     *
     * @return the warehouse pipe.
     */
    public Pipe getWarehousePipe() {
        return warehousePipe;
    }

    /**
     * Gets the total offcuts (remaining pipe length) after cutting.
     *
     * @return the offcuts.
     * @see CuttingMath#round(double)
     */
    public double getOffcuts() {
        return offcuts;
    }

    /**
     * Updates the {@code  offcuts} based on the current cut pieces.
     */
    private void updateOffcuts() {
        double piece = cuttingPipes.stream().mapToDouble(Pipe::getLength).sum();
        this.offcuts = CuttingMath.round(warehousePipe.getLength() - piece);
    }

    /**
     * Returns the number of cut pieces.
     *
     * @return the number of pieces cut.
     */
    public int getNumberPieces() {
        return getOffcuts() > 0 ? cuttingPipes.size() : Math.max(0, cuttingPipes.size() - 1);
    }

    /**
     * Returns a string representation of the cutting process.
     *
     * @return the string representation.
     * @see Truncation#form(String, boolean, double)
     */
    @Override
    public String toString() {
        return Truncation.form("", false, warehousePipe.getLength()) + " -> " + listCuttings() +
                " Offcuts: " + Truncation.form("", false, getOffcuts());
    }

    /**
     * Adds a new cut pipe to the cutting process.
     *
     * @param pipe the pipe to be added as a cut piece.
     */
    public void addCutting(Pipe pipe) {
        cuttingPipes.add(new Pipe(pipe));
        updateOffcuts();
    }

    /**
     * Calculates the total offcuts from a list of cutting processes.
     *
     * @param current the list of cutting processes.
     * @return the total offcuts.
     * @see CuttingMath#round(double)
     */
    public static double sumOffcuts(List<Cutting> current) {
        return CuttingMath.round(current.stream().mapToDouble(Cutting::getOffcuts).sum());
    }

    /**
     * Calculates the total number of pieces from a list of cutting processes.
     *
     * @param current the list of cutting processes.
     * @return the total number of pieces.
     */
    public static int sumNumberPieces(List<Cutting> current) {
        return current.stream().mapToInt(Cutting::getNumberPieces).sum();
    }

    /**
     * Sorts the list of cut pieces in descending order by their length.
     *
     * @see Comparator#comparingDouble(java.util.function.ToDoubleFunction)
     */
    public void sortCuttings() {
        cuttingPipes.sort(Comparator.comparingDouble(Pipe::getLength).reversed());
    }

    /**
     * Lists all the cut pieces in a string format.
     *
     * @return a string representation of the cut pieces.
     * @see Truncation#form(String, boolean, double)
     */
    private @NotNull String listCuttings() {
        if (cuttingPipes.isEmpty()) {
            return "No cuts";
        }
        return cuttingPipes.stream().map(pipe -> Truncation.form(
                "", false, pipe.getLength())).collect(Collectors.joining("; "));
    }
}
