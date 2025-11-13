package hsebank.interfaces;

import hsebank.domains.Storable;
import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface for CRUD operations on storable entities.
 *
 * @param <T> the type of storable entity managed by this repository
 */
public interface IRepository<T extends Storable> {

    /**
     * Retrieves all entities from the repository.
     *
     * @return a list of all entities
     */
    List<T> findAll();

    /**
     * Retrieves an entity by its ID.
     *
     * @param id the ID of the entity to retrieve
     * @return an Optional containing the entity if found, empty otherwise
     */
    Optional<T> findById(int id);

    /**
     * Saves an entity to the repository.
     * If the entity already exists, it will be updated.
     *
     * @param object the entity to save or update
     */
    void save(T object);

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     */
    void delete(int id);

    /**
     * Removes all entities from the repository.
     */
    void clear();
}