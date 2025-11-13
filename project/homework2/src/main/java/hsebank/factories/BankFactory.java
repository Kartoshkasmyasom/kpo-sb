package hsebank.factories;

import hsebank.domains.BankAccount;
import hsebank.domains.Category;
import hsebank.domains.Operation;
import hsebank.enums.CategoryType;
import hsebank.enums.OperationType;
import hsebank.interfaces.IDomainFactory;
import hsebank.params.AccountCreationParams;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Factory for creating domain objects in the banking system.
 * Implements the IDomainFactory interface to provide creation methods for bank entities.
 */
public class BankFactory implements IDomainFactory {

    /**
     * Creates a new BankAccount with the specified parameters.
     *
     * @param params the parameters for account creation
     * @return the created BankAccount
     */
    @Override
    public BankAccount createBankAccount(final AccountCreationParams params) {
        return new BankAccount(params.getName(), params.getBalance());
    }

    /**
     * Creates a new Category with the specified type and name.
     *
     * @param type the type of the category (INCOME/EXPENSE)
     * @param name the name of the category
     * @return the created Category
     * @throws IllegalArgumentException if type is invalid or name is null/empty
     */
    @Override
    public Category createCategory(final CategoryType type, final String name) {
        if (type != CategoryType.EXPENSE && type != CategoryType.INCOME) {
            throw new IllegalArgumentException(
                    "Category type must be specified. Available types: "
                            + Arrays.toString(CategoryType.values()));
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }
        return new Category(type, name.trim());
    }

    /**
     * Creates a new Operation with the specified parameters.
     *
     * @param type the type of operation (INCOME/EXPENSE)
     * @param bankAccountId the ID of the associated bank account
     * @param categoryId the ID of the associated category
     * @param amount the amount of the operation (must be positive)
     * @param date the date of the operation
     * @return the created Operation
     * @throws IllegalArgumentException if parameters are invalid
     */
    @Override
    public Operation createOperation(
            final OperationType type,
            final int bankAccountId,
            final int categoryId,
            final int amount,
            final LocalDate date) {
        if (type != OperationType.EXPENSE && type != OperationType.INCOME) {
            throw new IllegalArgumentException(
                    "Operation type must be specified. Available types: "
                            + Arrays.toString(OperationType.values()));
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (date == null) {
            throw new IllegalArgumentException("Operation date cannot be null");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Operation date cannot be in the future");
        }
        if (date.isBefore(LocalDate.now().minusYears(10))) {
            throw new IllegalArgumentException("Operation date cannot be older than 10 years");
        }

        return new Operation(type, bankAccountId, categoryId, amount, date);
    }
}