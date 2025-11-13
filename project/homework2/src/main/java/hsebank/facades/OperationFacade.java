package hsebank.facades;

import hsebank.domains.BankAccount;
import hsebank.domains.Category;
import hsebank.domains.Operation;
import hsebank.enums.CategoryType;
import hsebank.enums.OperationType;
import hsebank.factories.BankFactory;
import hsebank.interfaces.IRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Facade for operation management.
 * Provides a simplified interface for managing financial operations.
 */
public class OperationFacade {
    private final IRepository<Operation> operationRepository;
    private final BankAccountFacade bankAccountFacade;
    private final CategoryFacade categoryFacade;
    private final BankFactory factory;

    /**
     * Constructs an OperationFacade with the specified dependencies.
     *
     * @param operationRepository the repository for operation data storage
     * @param bankAccountFacade the facade for bank account operations
     * @param categoryFacade the facade for category operations
     * @param factory the factory for creating operation objects
     */
    public OperationFacade(
            final IRepository<Operation> operationRepository,
            final BankAccountFacade bankAccountFacade,
            final CategoryFacade categoryFacade,
            final BankFactory factory) {
        this.operationRepository = operationRepository;
        this.bankAccountFacade = bankAccountFacade;
        this.categoryFacade = categoryFacade;
        this.factory = factory;
    }

    /**
     * Creates a new financial operation.
     *
     * @param type the type of operation (INCOME/EXPENSE)
     * @param bankAccountId the ID of the associated bank account
     * @param categoryId the ID of the associated category
     * @param amount the amount of the operation
     * @param date the date of the operation
     * @return the created Operation
     * @throws IllegalArgumentException if account/category not found or types don't match
     * @throws RuntimeException if operation creation fails
     */
    public Operation create(
            final OperationType type,
            final int bankAccountId,
            final int categoryId,
            final int amount,
            final LocalDate date) {

        Optional<BankAccount> accOpt = bankAccountFacade.get(bankAccountId);
        Optional<Category> catOpt = categoryFacade.get(categoryId);
        if (accOpt.isEmpty() || catOpt.isEmpty()) {
            throw new IllegalArgumentException("Account or category not found");
        }
        if ((type == OperationType.INCOME && catOpt.get().getType() == CategoryType.EXPENSE)
                || (type == OperationType.EXPENSE && catOpt.get().getType() == CategoryType.INCOME)) {
            throw new IllegalArgumentException("Operation and category type should match");
        }

        Operation operation;
        try {
            operation = factory.createOperation(type, bankAccountId, categoryId, amount, date);
            operationRepository.save(operation);
        } catch (Exception e) {
            throw new RuntimeException("Could not create operation: " + e.getMessage());
        }
        applyOperationToAccount(operation);
        return operation;
    }

    /**
     * Retrieves all operations.
     *
     * @return a list of all operations
     */
    public List<Operation> list() {
        return operationRepository.findAll();
    }

    /**
     * Retrieves an operation by its ID.
     *
     * @param id the ID of the operation to retrieve
     * @return an Optional containing the operation if found, empty otherwise
     */
    public Optional<Operation> get(final int id) {
        return operationRepository.findById(id);
    }

    /**
     * Deletes an operation by its ID.
     *
     * @param id the ID of the operation to delete
     */
    public void delete(final int id) {
        operationRepository.delete(id);
    }

    /**
     * Applies the operation to the associated bank account.
     *
     * @param operation the operation to apply
     */
    private void applyOperationToAccount(final Operation operation) {
        if (operation.getType() == OperationType.INCOME) {
            bankAccountFacade.deposit(operation.getBankAccountId(), operation.getAmount());
        } else {
            bankAccountFacade.withdraw(operation.getBankAccountId(), operation.getAmount());
        }
    }
}