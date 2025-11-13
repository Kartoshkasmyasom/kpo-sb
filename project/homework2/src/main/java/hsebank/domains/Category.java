package hsebank.domains;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import hsebank.enums.CategoryType;
import hsebank.interfaces.ICategory;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a financial category for organizing and classifying operations. Each category has a
 * type (INCOME/EXPENSE) and a unique name for grouping similar transactions.
 */
public class Category extends Storable implements ICategory {

    @Getter
    private final CategoryType type;

    @Setter
    @Getter
    private String name;

    /**
     * Creates a new Category with specified type and name.
     *
     * @param type the category type (INCOME or EXPENSE)
     * @param name the name of the category (e.g., "Salary", "Groceries", "Entertainment")
     */
    @JsonCreator
    public Category(@JsonProperty("type") final CategoryType type,
                    @JsonProperty("name") final String name) {
        super();
        this.type = type;
        this.name = name.trim();
    }

    /**
     * Compares this category to the specified object.
     *
     * @param o the object to compare this Category against
     * @return true if the given object represents a Category with the same ID
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Category other = (Category) o;
        return id == other.id;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}