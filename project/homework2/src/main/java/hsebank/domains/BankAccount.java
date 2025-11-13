package hsebank.domains;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import hsebank.interfaces.IBankAccount;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a bank account with basic banking operations. Each account has a unique identifier,
 * holder name, and current balance. Provides functionality for account management and financial
 * transactions.
 */
public class BankAccount extends Storable implements IBankAccount {
    @Setter
    @Getter
    protected String name;

    @Setter
    @Getter
    protected int balance = 0;

    /**
     * Creates a new bank account with specified holder name and initial balance.
     *
     * @param name the name of the account holder
     * @param balance the initial account balance (must be non-negative)
     */
    @JsonCreator
    public BankAccount(@JsonProperty("name") final String name,
                       @JsonProperty("balance") final int balance) {
        super();
        this.name = name.trim();
        this.balance = balance;
    }

    /**
     * Compares this bank account to the specified object.
     *
     * @param o the object to compare this BankAccount against
     * @return true if the given object represents a BankAccount with the same ID
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankAccount other = (BankAccount) o;
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