package pipesag.optimizer;

import pipesag.datastructure.*;
import pipesag.utility.CuttingMath;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code MinOffcutsCalculator} class is responsible for optimizing the processing
 * of pipe orders within a warehouse setting. It calculates the minimal offcuts
 * resulting from cutting pipes to fulfill specific orders while considering
 * available pipe lengths in the warehouse.
 *
 * <p>This class utilizes an instance of the {@code Warehouse} class to access
 * the list of available pipes and employs a recursive approach to explore
 * potential cutting strategies that minimize waste. It generates solutions
 * based on a given {@code Order} and assesses the effectiveness of each
 * strategy through the calculation of offcuts.
 *
 * <p>Instances of this class can be created by passing a {@code Warehouse}
 * object to the constructor. The main method for calculation is
 * {@code calculate(Order order)}, which processes the provided order and
 * returns a {@code Processing} object that contains the optimal cutting strategy
 * with minimal offcuts.
 *
 * @see Warehouse
 * @see Order
 * @see Processing
 * @see Cutting
 */
public class MinOffcutsCalculator {

    private static final Logger LOGGER = Logger.getLogger(MinOffcutsCalculator.class.getName());

    private final Warehouse warehouse;

    /**
     * Constructs a {@code MinOffcutsCalculator} with the specified warehouse.
     *
     * @param warehouse the {@code Warehouse} instance containing available pipes
     */
    public MinOffcutsCalculator(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * Calculates the optimal processing strategy for the given order by minimizing
     * offcuts. This method logs the start and end time of the optimization process
     * and returns the best processing strategy found.
     *
     * @param order the {@code Order} to be processed
     * @return the {@code Processing} object representing the optimal solution
     */
    public Processing calculate(Order order) {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Started the optimization of processing procedure ");
        List<Pipe> remainingOrder = order.sortedOrders();
        Processing[] bestSolution = createEmptySolutionContainer();
        computeOffcuts(order, remainingOrder, bestSolution, null, -1);
        long endTime = System.currentTimeMillis();
        String lineSeparator = System.lineSeparator();
        LOGGER.log(Level.INFO, "Finished the optimization of processing procedure of the order \"{0}{1}{2}{3}\"ms!\"{4}\" optimal solution:{5}{6}",
                new Object[]{lineSeparator, order, lineSeparator, endTime -startTime,lineSeparator, lineSeparator, bestSolution[0]});
        return bestSolution[0];
    }

    /**
     * Recursively computes the offcuts for the given order and remaining pipes.
     * It updates the currently best machining strategy if a better solution is found.
     *
     * @param order the {@code Order} being processed
     * @param remainingOrder the list of remaining pipes to be cut
     * @param currentlyBestMachining an array holding the best processing solution found
     * @param currentCuttings the list of current cuttings made
     * @param rest the remaining length of pipe after cuts
     */
    void computeOffcuts(Order order, List<Pipe> remainingOrder, Processing[] currentlyBestMachining,
                                     List<Cutting> currentCuttings, double rest) {

        if (remainingOrder.isEmpty()) {
            if (isCurrentBetter(currentlyBestMachining[0], currentCuttings)) {
                currentlyBestMachining[0] = new Processing(order, currentCuttings);
            }
            return;
        }
        if (currentPotentialBetter(currentlyBestMachining[0], currentCuttings, rest)) {
            exploreRemainingLimb(order, remainingOrder, currentlyBestMachining, currentCuttings, rest);
        }
    }

    /**
     * Explores all possible cutting strategies for the remaining pipes. It
     * considers both the remaining order and the available pipes in the warehouse
     * to generate new cutting strategies.
     *
     * @param order the {@code Order} being processed
     * @param remainingOrder the list of remaining pipes to be cut
     * @param currentlyBestCutting an array holding the best cutting strategy found
     * @param currentCuttings the list of current cuttings made
     * @param restOrder the remaining length of pipe after cuts
     */
    private void exploreRemainingLimb(Order order, List<Pipe> remainingOrder, Processing[] currentlyBestCutting,
                                      List<Cutting> currentCuttings, double restOrder) {
        remainingOrder.forEach(pipe -> {
            if (restOrder > pipe.getLength() || CuttingMath.almostEqual(restOrder, pipe.getLength())) {
                double newRestOrder = restOrder - pipe.getLength();
                computeOffcuts(order, ordersWithoutCurrentPipe(remainingOrder, pipe), currentlyBestCutting,
                        updateCreateNewCuttingWithPipe(currentCuttings, pipe), newRestOrder);
            }
            warehouse.getPipeList().forEach(wPipe -> {
                if (wPipe.getLength() > pipe.getLength() || CuttingMath.almostEqual(pipe.getLength(), wPipe.getLength())) {
                    double newRestOrder = wPipe.getLength() - pipe.getLength();
                    computeOffcuts(order, ordersWithoutCurrentPipe(remainingOrder, pipe), currentlyBestCutting,
                            createNewCuttingWithPipe(currentCuttings, pipe, wPipe), newRestOrder);
                }
            });
        });
    }

    /**
     * Updates the current list of cuttings by adding a new cutting for the given pipe.
     *
     * @param currentCuttings the list of current cuttings made
     * @param pipe the {@code Pipe} being added to the current cuttings
     * @return a new list of cuttings with the added pipe
     */
    private List<Cutting> updateCreateNewCuttingWithPipe(List<Cutting> currentCuttings, Pipe pipe) {
        List<Cutting> updateCuttings = deepCopy(currentCuttings);
        updateCuttings.getLast().addCutting(new Pipe(pipe));
        return updateCuttings;
    }

    /**
     * Creates a deep copy of the current cuttings list to avoid modifying the original list.
     *
     * @param currentCuttings the list of current cuttings made
     * @return a deep copy of the current cuttings list
     */
    private List<Cutting> deepCopy(List<Cutting> currentCuttings) {
        List<Cutting> copiedCuttings = new ArrayList<>(currentCuttings.size());
        currentCuttings.forEach(cutting -> copiedCuttings.add(new Cutting(cutting)));
        return copiedCuttings;
    }

    /**
     * Creates a new cutting strategy by adding the given pipe and warehouse pipe to the
     * current cuttings.
     *
     * @param currentCuttings the list of current cuttings made
     * @param pipe the {@code Pipe} being added to the new cutting
     * @param wPipe the warehouse pipe associated with the new cutting
     * @return a new list of cuttings including the new cutting
     */
    private List<Cutting> createNewCuttingWithPipe(List<Cutting> currentCuttings, Pipe pipe, Pipe wPipe) {
        List<Cutting> updateCuttings = currentCuttings == null ? new ArrayList<>() : deepCopy(currentCuttings);
        Cutting newCutting = new Cutting(wPipe);
        newCutting.addCutting(new Pipe(pipe));
        updateCuttings.add(newCutting);
        return updateCuttings;
    }

    /**
     * Returns a new list of remaining pipes excluding the specified pipe from the current order.
     *
     * @param remainingOrder the list of remaining pipes to be cut
     * @param pipe the {@code Pipe} to be excluded
     * @return a new list of pipes excluding the specified pipe
     */
    private List<Pipe> ordersWithoutCurrentPipe(List<Pipe> remainingOrder, Pipe pipe) {
//        List<Pipe> orderPipes = new ArrayList<>(remainingOrder.size());
//        remainingOrder.forEach(orderPipe -> {
//            if (!pipe.equals(orderPipe)) {
//                orderPipes.add(new Pipe(orderPipe));
//            }
//        });
//        return orderPipes;
        return remainingOrder.stream().filter(orederPipe -> !pipe.equals(orederPipe)).map(Pipe::new).toList();
    }

    /**
     * Checks if the current potential solution is better than the previously best solution.
     *
     * @param bestProcessing the best {@code Processing} solution found so far
     * @param currentCuttings the list of current cuttings made
     * @param rest the remaining length of pipe after cuts
     * @return true if the current potential solution is better, false otherwise
     */
    private boolean currentPotentialBetter(Processing bestProcessing, List<Cutting> currentCuttings, double rest) {
        if (bestProcessing == null || currentCuttings == null) {
            return true;
        }
        double currentOffcuts = Cutting.sumOffcuts(currentCuttings);
        double minOffcuts = bestProcessing.getOffcuts();
        boolean isPotentialBetter = Cutting.sumNumberPieces(currentCuttings) < bestProcessing.getNrCuttings();
        isPotentialBetter &= Math.abs(rest) < 0.0000001;
        isPotentialBetter &= Math.abs(currentOffcuts - minOffcuts) <= 0.1;
        isPotentialBetter |= currentOffcuts - minOffcuts < rest;
        return isPotentialBetter;
    }

    /**
     * Determines if the current cuttings are better than the previously best cuttings.
     *
     * @param bestProcessing the best {@code Processing} solution found so far
     * @param currentCuttings the list of current cuttings made
     * @return true if the current cuttings are better, false otherwise
     */
    private boolean isCurrentBetter(Processing bestProcessing, List<Cutting> currentCuttings) {
        if (bestProcessing == null || currentCuttings == null) {
            return true;
        }
        double currentOffcuts = Cutting.sumOffcuts(currentCuttings);
        double minOffcuts = bestProcessing.getOffcuts();
        int sumNrPieces = Cutting.sumNumberPieces(currentCuttings);
        int nrCuttings = bestProcessing.getNrCuttings();
        boolean isBetter = Math.abs(currentOffcuts - minOffcuts) <= 0.0001;
        isBetter &=  sumNrPieces < nrCuttings;
        isBetter |= currentOffcuts < minOffcuts;
        return isBetter;
    }

    /**
     * Creates an empty container for holding the best processing solution found.
     *
     * @return an array containing a single empty {@code Processing} object
     */
    private Processing[] createEmptySolutionContainer() {
        return new Processing[]{null}; // Explicitly returns an empty Optional if no solution exists
    }
}
