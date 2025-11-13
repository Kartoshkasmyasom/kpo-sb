package hsebank.facades;

import hsebank.domains.Category;
import hsebank.enums.CategoryType;
import hsebank.factories.BankFactory;
import hsebank.interfaces.IRepository;
import java.util.List;
import java.util.Optional;

/**
 * Facade for category operations.
 * Provides a simplified interface for managing categories.
 */
public class CategoryFacade {
    private final IRepository<Category> repository;
    private final BankFactory factory;

    /**
     * Constructs a CategoryFacade with the specified repository and factory.
     *
     * @param repository the repository for category data storage
     * @param factory the factory for creating category objects
     */
    public CategoryFacade(final IRepository<Category> repository,
                          final BankFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    /**
     * Creates a new category with the specified type and name.
     *
     * @param type the type of the category (INCOME/EXPENSE)
     * @param name the name for the new category
     * @return the created Category
     * @throws RuntimeException if category creation fails
     */
    public Category create(final CategoryType type, final String name) {
        try {
            Category category = factory.createCategory(type, name);
            repository.save(category);
            return category;
        } catch (Exception e) {
            throw new RuntimeException("Could not create category: " + e.getMessage());
        }
    }

    /**
     * Retrieves all categories.
     *
     * @return a list of all categories
     */
    public List<Category> list_all_categories() {
        return repository.findAll();
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category to retrieve
     * @return an Optional containing the category if found, empty otherwise
     */
    public Optional<Category> get(final int id) {
        return repository.findById(id);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     */
    public void delete(final int id) {
        repository.delete(id);
    }

    /**
     * Renames a category.
     *
     * @param categoryId the ID of the category to rename
     * @param newName the new name for the category (cannot be null or empty)
     * @throws IllegalArgumentException if newName is null/empty or category not found
     */
    public void rename(final int categoryId, final String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        String clean = newName.trim();
        Category category = repository
                .findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.setName(clean);
        repository.save(category);
    }
}