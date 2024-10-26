package pipesag.datastructure;

import pipesag.utility.Truncation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Represents the processing of an order which includes cutting pipes to meet the order requirements.
 *
 * @see Order
 * @see Cutting
 */
public class Processing {

    protected Order order;
    private List<Cutting> cuttings;

    /**
     * Default constructor.
     */
    public Processing() {

    }

    /**
     * Creates a processing operation for a specific order and cutting process list.
     *
     * @param order the order to process.
     * @param cuttings the list of cutting processes.
     */
    public Processing(Order order, List<Cutting> cuttings) {
        this.order = new Order(order);
        this.cuttings = new ArrayList<>();
///        if(cuttings != null) {
//            cuttings.forEach(c -> {
//                c.sortCuttings();
//                this.cuttings.add(new Cutting(c));
//            });
//        }
        Optional.ofNullable(cuttings).ifPresent(cuttingList ->cuttingList.forEach(c -> {
            c.sortCuttings();
            this.cuttings.add(new Cutting(c));
        }));
        this.cuttings.sort(Comparator.comparingDouble(Cutting::getOffcuts).thenComparing(
                Comparator.comparingDouble((Cutting c ) -> c.getWarehousePipe().getLength()).reversed().thenComparing(
                        Comparator.comparingInt(Cutting::getNumberPieces).reversed()                                 )
        ));
    }

    /**
     * Returns a string representation of the processing.
     * Includes the order, total offcuts, and number of cuttings.
     *
     * @return the string representation of the processing.
     */
    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        stb.append(order.toString()).append(System.lineSeparator());
        stb.append(Truncation.form("Offcuts: ", true, getOffcuts()));
        stb.append(Truncation.form("Number of Cuttings: ", true, getNrCuttings()));
        cuttings.forEach(c -> {stb.append(Truncation.form("","s", true, c.toString()));});
        if (!cuttings.isEmpty()) {
            stb.delete(stb.length() - System.lineSeparator().length(), stb.length());
        }
        return stb.toString();
    }

    /**
     * Calculates the total number of pieces cut across all cuttings.
     *
     * @return the total number of pieces.
     */
    public int getNrCuttings() {
        return Cutting.sumNumberPieces(cuttings);
    }
//    public int getNrCuttings() {
//        return cuttings.stream().mapToInt(Cutting::getNumberPieces).sum();
//    }


    /**
     * Calculates the total offcuts from all cuttings.
     *
     * @return the total offcuts.
     */
    public double getOffcuts() {
        return cuttings.stream().mapToDouble(Cutting::getOffcuts).sum();
    }

    /**
     * Compares two processing operations to see if they have the same quality (same offcuts and number of cuttings).
     *
     * @param that the processing operation to compare with.
     * @return {@code true} if both processing operations have the same quality, {@code false} otherwise.
     */
    public boolean hasSameQuality(Processing that) {
        return this.getOffcuts() == that.getOffcuts() && this.getNrCuttings() == that.getNrCuttings();
    }

    /**
     * Gets the order being processed.
     *
     * @return the order.
     * @see Order
     */
    public Order getOrder() {
        return order;
    }
}
