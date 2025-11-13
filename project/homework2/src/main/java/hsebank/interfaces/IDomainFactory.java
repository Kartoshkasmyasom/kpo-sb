package hsebank.interfaces;

import hsebank.domains.BankAccount;
import hsebank.domains.Category;
import hsebank.domains.Operation;
import hsebank.enums.CategoryType;
import hsebank.enums.OperationType;
import hsebank.params.AccountCreationParams;
import java.time.LocalDate;

/**
 * Factory interface for creating domain objects in the banking system.
 * Provides methods for creating bank accounts, categories, and operations.
 */
public interface IDomainFactory {

    /**
     * Creates a new bank account with the specified parameters.
     *
     * @param params the parameters for account creation
     * @return the created BankAccount
     */
    BankAccount createBankAccount(AccountCreationParams params);

    /**
     * Creates a new category with the specified type and name.
     *
     * @param type the type of the category (INCOME/EXPENSE)
     * @param name the name of the category
     * @return the created Category
     */
    Category createCategory(CategoryType type, String name);

    /**
     * Creates a new operation with the specified parameters.
     *
     * @param type the type of operation (INCOME/EXPENSE)
     * @param bankAccountId the ID of the associated bank account
     * @param categoryId the ID of the associated category
     * @param amount the amount of the operation
     * @param date the date of the operation
     * @return the created Operation
     */
    Operation createOperation(
            OperationType type,
            int bankAccountId,
            int categoryId,
            int amount,
            LocalDate date);
}