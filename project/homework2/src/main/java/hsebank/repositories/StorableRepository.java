package hsebank.repositories;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hsebank.domains.Storable;
import hsebank.interfaces.IRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JSON-based repository implementation for storable entities.
 * Stores data in JSON files with support for Java time types.
 *
 * @param <T> the type of storable entity managed by this repository
 */
public class StorableRepository<T extends Storable> implements IRepository<T> {
    private final Path dbPath;
    private ObjectMapper mapper;
    private Class<T> typeClass;

    /**
     * Constructs a StorableRepository with the specified database file and entity class.
     *
     * @param dbName the name of the JSON database file
     * @param typeClass the class type of the stored entities
     */
    public StorableRepository(String dbName, Class<T> typeClass) {
        this.dbPath = Paths.get("hsebank", "data", dbName).toAbsolutePath();
        this.mapper = createObjectMapper();
        initializeRepository();
        this.typeClass = typeClass;
    }

    /**
     * Creates and configures an ObjectMapper for JSON serialization.
     *
     * @return the configured ObjectMapper instance
     */
    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    /**
     * Initializes the repository by creating necessary directories and files.
     */
    private void initializeRepository() {
        try {
            createDirectories();
            initializeFile();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to initialize repository: " + dbPath + " " + e.getMessage());
        }
    }

    /**
     * Creates the directory structure for the database file if it doesn't exist.
     *
     * @throws IOException if directory creation fails
     */
    private void createDirectories() throws IOException {
        Path parentDir = dbPath.getParent();
        if (parentDir != null && !Files.exists(dbPath)) {
            Files.createDirectories(parentDir);
        }
    }

    /**
     * Initializes the database file with an empty array if it doesn't exist.
     *
     * @throws IOException if file creation fails
     */
    private void initializeFile() throws IOException {
        if (!Files.exists(dbPath)) {
            mapper.writeValue(dbPath.toFile(), new ArrayList<T>());
        }
    }

    /**
     * Retrieves all entities from the JSON database.
     *
     * @return a list of all entities
     * @throws RuntimeException if reading from the database fails
     */
    @Override
    public List<T> findAll() {
        try {
            JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, typeClass);
            return mapper.readValue(dbPath.toFile(), type);
        } catch (IOException e) {
            throw new RuntimeException("I/O error: Failed to read " + dbPath + " " + e.getMessage());
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * Retrieves an entity by its ID from the JSON database.
     *
     * @param id the ID of the entity to retrieve
     * @return an Optional containing the entity if found, empty otherwise
     */
    @Override
    public Optional<T> findById(int id) {
        List<T> items = findAll();
        return items.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    /**
     * Saves an entity to the JSON database.
     * If the entity already exists, it will be updated.
     *
     * @param object the entity to save or update
     * @throws RuntimeException if writing to the database fails
     */
    @Override
    public void save(T object) {
        try {
            List<T> items = findAll();
            items.removeIf(x -> x.getId() == object.getId());
            items.add(object);
            mapper.writerWithDefaultPrettyPrinter().writeValue(dbPath.toFile(), items);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write into db " + dbPath, e);
        }
    }

    /**
     * Deletes an entity by its ID from the JSON database.
     *
     * @param id the ID of the entity to delete
     * @throws RuntimeException if deletion from the database fails
     */
    @Override
    public void delete(int id) {
        try {
            List<T> items = findAll();
            boolean removed = items.removeIf(x -> x.getId() == id);
            if (!removed) {
                System.out.println("No item with id=" + id + " to delete");
                return;
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(dbPath.toFile(), items);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete from db " + dbPath, e);
        }
    }

    /**
     * Clears all entities from the JSON database.
     */
    @Override
    public void clear() {
        List<T> list = findAll();
        for (T obj : list) {
            delete(obj.getId());
        }
    }
}