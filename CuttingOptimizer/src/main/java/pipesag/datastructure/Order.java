package pipesag.datastructure;

import pipesag.utility.Truncation;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents an order made by a customer, containing pipes and
 * their respective quantities.
 *
 * @see Pipe
 * @see Customer
 */
public class Order {

    private final Customer customer;

    private final List<Map.Entry<Pipe, Integer>> items;

    /**
     * Creates an Order for a specific customer.
     *
     * @param customer the customer who placed the order
     */
    public Order(Customer customer) {
        this.customer = customer;
        this.items = new ArrayList<>();
    }

    /**
     * Copy constructor that creates a new order from an existing order.
     *
     * @param other the Order to copy from
     */
    public Order(Order other) {
        this.customer = other.getCustomer();
        this.items = new ArrayList<>(other.items);
        items.addAll(other.items);
    }

    /**
     * Gets the customer who placed the order.
     *
     * @return the customer associated with this order.
     * @see Customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Gets the list of items ({@link Pipe}s and their quantities) in this order.
     * The list is unmodifiable.
     *
     * @return the list of items in the order.
     * @see Collections#unmodifiableList(List)
     */
    public List<Map.Entry<Pipe, Integer>> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Adds a {@link Pipe} and its quantity to the order.
     * If the {@code  pipe} exist already, its quantity
     * in the corresponding item will be only added.
     * @param pipe the pipe to be added.
     * @param quantity the quantity of the pipe.
     */
    public void addItem(Pipe pipe, int quantity) {
//        boolean exists = items.stream().anyMatch(i -> i.getKey().equals(pipe));
        Optional<Map.Entry<Pipe, Integer>> existingItem = items.stream().filter(item -> item.getKey().equals(pipe)).findFirst();
        if (existingItem.isPresent()) {
            Map.Entry<Pipe, Integer> item = existingItem.get();
            item.setValue(item.getValue() + quantity);
        } else {
            items.add(new AbstractMap.SimpleEntry<>(pipe, quantity));
        }
    }

    /**
     * Returns a string representation of the order.
     * Includes the customer and the list of items.
     *
     * @return the string representation of the order.
     * @see StringBuilder
     */
    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        stb.append(customer.toString()).append(System.lineSeparator());
        if (items.isEmpty()) {
            stb.append(System.lineSeparator()).append("No Items ordered!");
        } else {
            AtomicInteger counter = new AtomicInteger();
            items.forEach(item -> {
                if (counter.get() != 0 && counter.get() % 10 == 0) {
                    stb.append(System.lineSeparator());
                }
                stb.append(item.getValue()).append("*").append(Truncation.form("", false, item.getKey().getLength())).append(";");
                if (counter.incrementAndGet() % 10 == 0) {
                    stb.delete(stb.length() - 2, stb.length()); // remove "; " at the end of line.
                }
            });

            stb.delete(stb.length() - 2, stb.length());
            stb.append(System.lineSeparator());
        }
        return stb.toString();
    }

    /**
     * Compares this order to another order to check for equality.
     * Orders are considered equal if they have the same customer and the same items.
     *
     * @param other the object to compare with this order.
     * @return {@code true} if the orders are equal, {@code false} otherwise.
     * @see Objects#equals(Object, Object)
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other == null || getClass() != other.getClass()) {
            return false;
        } else {
            Order that = (Order) other;
            boolean equals = this.customer.equals(that.getCustomer());

            equals &= this.items.size() == that.getItems().size();
            List<Pipe> thatItem = that.sortedOrders();
            List<Pipe> thisItem = this.sortedOrders();
            for (int i = 0; i < thatItem.size() && equals; i++) {
                equals = thisItem.get(i).equals(thatItem.get(i));
            }
            return equals;
        }
    }


    /**
     * Returns a sorted list of pipes based on their lengths in descending order.
     *
     * @return the sorted list of pipes.
     * @see Pipe#getLength()
     * @see Comparator#comparingDouble(java.util.function.ToDoubleFunction)
     */
    public List<Pipe> sortedOrders() {
        List<Pipe> result = new ArrayList<>();
        this.items.forEach(item -> {
            for (int i = 0; i < item.getValue(); i++) {
                result.add(new Pipe(item.getKey())); // Using new Constructor to avoid any affect in the reference origin
            }
        });
        result.sort(Comparator.comparingDouble(Pipe::getLength).reversed());
        return result;
    }
}