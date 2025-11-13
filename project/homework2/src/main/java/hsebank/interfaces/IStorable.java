package hsebank.interfaces;

/**
 * Marker interface for storable entities in the system.
 * Provides a common interface for objects that can be stored and retrieved by their unique identifier.
 */
public interface IStorable {

    /**
     * Gets the unique identifier of the storable object.
     *
     * @return the unique ID of the object
     */
    int getId();
}