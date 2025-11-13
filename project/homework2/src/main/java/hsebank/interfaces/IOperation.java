package hsebank.interfaces;

import hsebank.enums.OperationType;
import java.time.LocalDate;

/**
 * Interface for bank operations.
 * Provides methods for accessing and managing operation properties.
 */
public interface IOperation extends IStorable {

    /**
     * Gets the type of the operation.
     *
     * @return the operation type (INCOME/EXPENSE)
     */
    OperationType getType();

    /**
     * Gets the ID of the associated bank account.
     *
     * @return the bank account ID
     */
    int getBankAccountId();

    /**
     * Gets the ID of the associated category.
     *
     * @return the category ID
     */
    int getCategoryId();

    /**
     * Gets the amount of the operation.
     *
     * @return the operation amount
     */
    int getAmount();

    /**
     * Gets the date of the operation.
     *
     * @return the operation date
     */
    LocalDate getDate();

    /**
     * Gets the description of the operation.
     *
     * @return the operation description
     */
    String getDescription();

    /**
     * Sets the description of the operation.
     *
     * @param description the new description to set
     */
    void setDescription(String description);
}