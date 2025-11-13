package hsebank.params;

/**
 * Parameters for creating a new bank account.
 * Uses the Builder pattern for flexible object creation.
 */
public class AccountCreationParams {
    private final String name;
    private final int balance;

    /**
     * Constructs AccountCreationParams from the builder.
     *
     * @param builder the builder containing account parameters
     */
    private AccountCreationParams(Builder builder) {
        this.name = builder.name;
        this.balance = builder.balance;
    }

    /**
     * Builder class for AccountCreationParams.
     * Provides a fluent interface for constructing account parameters.
     */
    public static class Builder {
        private final String name;
        private int balance = 0;

        /**
         * Constructs a Builder with the required account name.
         *
         * @param name the account name (cannot be null or empty)
         * @throws IllegalArgumentException if name is null or empty
         */
        public Builder(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Account name cannot be null or empty");
            }
            this.name = name.trim();
        }

        /**
         * Sets the account balance.
         *
         * @param balance the initial balance (cannot be negative)
         * @return the builder instance for method chaining
         * @throws IllegalArgumentException if balance is negative
         */
        public Builder balance(int balance) {
            if (balance < 0) {
                throw new IllegalArgumentException("Balance cannot be negative");
            }
            this.balance = balance;
            return this;
        }

        /**
         * Builds the AccountCreationParams instance.
         *
         * @return the constructed AccountCreationParams
         */
        public AccountCreationParams build() {
            return new AccountCreationParams(this);
        }
    }

    /**
     * Gets the account name.
     *
     * @return the account name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the account balance.
     *
     * @return the account balance
     */
    public int getBalance() {
        return balance;
    }
}