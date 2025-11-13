package hsebank.interfaces;

import hsebank.enums.CategoryType;

/**
 * Interface for basic category operations.
 * Provides methods for managing category type and name.
 */
public interface ICategory extends IStorable {

    /**
     * Gets the type of the category.
     *
     * @return the category type (INCOME/EXPENSE)
     */
    CategoryType getType();

    /**
     * Sets the name of the category.
     *
     * @param name the new name to set
     */
    void setName(String name);

    /**
     * Gets the name of the category.
     *
     * @return the category name
     */
    String getName();
}