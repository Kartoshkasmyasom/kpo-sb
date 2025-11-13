package hsebank.facades;

import hsebank.domains.BankAccount;
import hsebank.factories.BankFactory;
import hsebank.interfaces.IRepository;
import hsebank.params.AccountCreationParams;
import java.util.List;
import java.util.Optional;

/**
 * Facade for bank account operations.
 * Provides a simplified interface for managing bank accounts.
 */
public class BankAccountFacade {
    private final IRepository<BankAccount> repository;
    private final BankFactory factory;

    /**
     * Constructs a BankAccountFacade with the specified repository and factory.
     *
     * @param repository the repository for bank account data storage
     * @param factory the factory for creating bank account objects
     */
    public BankAccountFacade(final IRepository<BankAccount> repository,
                             final BankFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    /**
     * Creates a new bank account with the specified name and balance.
     *
     * @param name the name for the new account
     * @param balance the initial balance for the new account
     * @return the created BankAccount
     * @throws RuntimeException if account creation fails
     */
    public BankAccount create(final String name, final int balance) {
        try {
            AccountCreationParams params = new AccountCreationParams.Builder(name)
                    .balance(balance)
                    .build();
            BankAccount account = factory.createBankAccount(params);
            repository.save(account);
            return account;
        } catch (Exception e) {
            throw new RuntimeException("Could not create new bank account: " + e.getMessage());
        }
    }

    /**
     * Retrieves all bank accounts.
     *
     * @return a list of all bank accounts
     */
    public List<BankAccount> list_all_accounts() {
        return repository.findAll();
    }

    /**
     * Retrieves a bank account by its ID.
     *
     * @param id the ID of the account to retrieve
     * @return an Optional containing the account if found, empty otherwise
     */
    public Optional<BankAccount> get(final int id) {
        return repository.findById(id);
    }

    /**
     * Deletes a bank account by its ID.
     *
     * @param id the ID of the account to delete
     */
    public void delete(final int id) {
        repository.delete(id);
    }

    /**
     * Deposits the specified amount into the account.
     *
     * @param accountId the ID of the account to deposit to
     * @param amount the amount to deposit (must be positive)
     * @throws IllegalArgumentException if amount is not positive or account not found
     * @throws IllegalStateException if the new balance would be negative
     */
    public void deposit(final int accountId, final int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        BankAccount acc = repository
                .findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        int newBalance = acc.getBalance() + amount;
        if (newBalance < 0) {
            throw new IllegalStateException("Too much money for one individual");
        }
        acc.setBalance(newBalance);
        repository.save(acc);
    }

    /**
     * Withdraws the specified amount from the account.
     *
     * @param accountId the ID of the account to withdraw from
     * @param amount the amount to withdraw (must be positive)
     * @throws IllegalArgumentException if amount is not positive or account not found
     * @throws IllegalStateException if insufficient funds
     */
    public void withdraw(final int accountId, final int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        BankAccount acc = repository
                .findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        int newBalance = acc.getBalance() - amount;
        if (newBalance < 0) {
            throw new IllegalStateException("Not enough funds");
        }
        acc.setBalance(newBalance);
        repository.save(acc);
    }

    /**
     * Renames a bank account.
     *
     * @param accountId the ID of the account to rename
     * @param newName the new name for the account (cannot be null or empty)
     * @throws IllegalArgumentException if newName is null/empty or account not found
     */
    public void rename(final int accountId, final String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account name cannot be null or empty");
        }
        String clean = newName.trim();
        BankAccount acc = repository
                .findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("account not found"));
        acc.setName(clean);
        repository.save(acc);
    }
}