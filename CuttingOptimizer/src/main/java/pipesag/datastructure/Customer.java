package pipesag.datastructure;


import java.util.Objects;

/**
 * Represents a Customer with an ID and a name.
 *
 * @see Order
 */
public class Customer {

    private final int id;
    private final String name;

    /**
     * Default constructor initializing a Customer with ID 0 and an empty name.
     */
    public Customer() {
        this(0, "");
    }

    /**
     * Constructs a Customer with the specified ID and name.
     *
     * @param id the unique identifier for the customer, must be non-negative
     * @param name the name of the customer
     * @throws IllegalArgumentException if the {@code id < 0}.
     */
    public Customer(int id, String name) {
        if (id < 0){
            throw new IllegalArgumentException(" The Id  of the customer could not be negative");
        }
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the ID of this customer.
     *
     * @return the customer ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of this customer.
     *
     * @return the customer's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of this customer in the format {@code Customer [id=X, name=Y]}.
     *
     * @return a string in the format "Customer [id=..., name=...]"
     */
    @Override
    public String toString() {
        return "Customer [id=" + id + ", name=" + name + "]";
    }

    /**
     * Checks if this customer is equal to another object.
     * Two customers are considered equal if they have the same ID and name.
     *
     * @param other the object to compare with this customer.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     * @see Objects#equals(Object, Object)
     */
    @Override
    public boolean equals(Object other) {
        if (this == other){
            return true;
        } else if (other == null || this.getClass() != other.getClass()){
            return false;
        } else {
            Customer that = (Customer) other;
            boolean result = Objects.equals(this.id, that.id);
            result &= Objects.equals(this.name, that.name);
            return result;
        }
    }
}
