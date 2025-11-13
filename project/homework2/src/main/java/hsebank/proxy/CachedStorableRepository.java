package hsebank.proxy;

import hsebank.domains.Storable;
import hsebank.interfaces.IRepository;
import hsebank.repositories.StorableRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Cached repository implementation that wraps a StorableRepository.
 * Provides caching functionality to reduce expensive I/O operations.
 *
 * @param <T> the type of storable entity managed by this repository
 */
public class CachedStorableRepository<T extends Storable> implements IRepository<T> {
    private final StorableRepository<T> storableRepository;
    private List<T> storedObjects;
    private boolean needReset = false;

    /**
     * Constructs a CachedStorableRepository with the specified underlying repository.
     *
     * @param storableRepository the underlying repository to cache
     */
    public CachedStorableRepository(StorableRepository<T> storableRepository) {
        this.storableRepository = storableRepository;
        this.storedObjects = new ArrayList<>();
    }

    /**
     * Updates the cache by loading data from the underlying repository if needed.
     * Resets the cache if previous operations failed.
     */
    private void update() {
        try {
            if (storedObjects.isEmpty() || needReset) {
                storedObjects.clear();
                storedObjects.addAll(storableRepository.findAll());
            }
            needReset = false;
        } catch (Exception e) {
            needReset = true;
            throw new RuntimeException("Error in update of db cache: " + e.getMessage());
        }
    }

    /**
     * Retrieves all entities from the cache.
     *
     * @return a list of all cached entities
     */
    @Override
    public List<T> findAll() {
        update();
        return List.copyOf(storedObjects);
    }

    /**
     * Retrieves an entity by its ID from the cache.
     *
     * @param id the ID of the entity to retrieve
     * @return an Optional containing the entity if found, empty otherwise
     */
    @Override
    public Optional<T> findById(int id) {
        update();
        return storedObjects.stream()
                .filter(acc -> acc.getId() == id)
                .findFirst();
    }

    /**
     * Saves an entity to both the cache and the underlying repository.
     *
     * @param object the entity to save or update
     */
    @Override
    public void save(T object) {
        update();

        try {
            storableRepository.save(object);
            storedObjects.removeIf(item -> item.getId() == object.getId());
            storedObjects.add(object);
        } catch (Exception e) {
            needReset = true;
            throw e;
        }
    }

    /**
     * Deletes an entity by its ID from both the cache and the underlying repository.
     *
     * @param id the ID of the entity to delete
     */
    public void delete(int id) {
        update();
        try {
            storableRepository.delete(id);
            storedObjects.removeIf(acc -> acc.getId() == id);
        } catch (Exception e) {
            needReset = true;
            throw e;
        }
    }

    /**
     * Clears all entities from both the cache and the underlying repository.
     */
    public void clear() {
        update();
        try {
            storableRepository.clear();
            storedObjects.clear();
        } catch (Exception e) {
            needReset = true;
            throw e;
        }
    }
}