package hsebank.interfaces;

/**
 * Interface for basic bank account operations.
 * Provides methods for managing account balance and name.
 */
public interface IBankAccount extends IStorable {

    /**
     * Sets the balance of the bank account.
     *
     * @param balance the new balance to set
     */
    void setBalance(int balance);

    /**
     * Gets the current balance of the bank account.
     *
     * @return the current balance
     */
    int getBalance();

    /**
     * Sets the name of the bank account.
     *
     * @param name the new name to set
     */
    void setName(String name);

    /**
     * Gets the name of the bank account.
     *
     * @return the account name
     */
    String getName();
}