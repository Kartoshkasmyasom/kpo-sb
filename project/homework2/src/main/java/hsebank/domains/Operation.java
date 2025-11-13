package hsebank.domains;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import hsebank.enums.OperationType;
import hsebank.interfaces.IOperation;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a financial operation (transaction) in the banking system.
 * Contains information about operation type, associated bank account,
 * amount, date, optional description, and category.
 */
public class Operation extends Storable implements IOperation {

    @Getter
    private final OperationType type;

    @Getter
    private final int bankAccountId;

    @Getter
    private final int categoryId;

    @Getter
    private final int amount;

    @Getter
    private final LocalDate date;

    @Setter
    @Getter
    private String description;

    /**
     * Constructs an Operation with the specified parameters.
     *
     * @param type the type of operation (INCOME/EXPENSE)
     * @param bankAccountId the ID of the associated bank account
     * @param categoryId the ID of the associated category
     * @param amount the amount of the operation
     * @param date the date when the operation occurred
     */
    @JsonCreator
    public Operation(
            @JsonProperty("type") final OperationType type,
            @JsonProperty("bankAccountId") final int bankAccountId,
            @JsonProperty("categoryId") final int categoryId,
            @JsonProperty("amount") final int amount,
            @JsonProperty("date") final LocalDate date) {
        super();
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.description = "";
    }

    /**
     * Compares this operation to the specified object.
     *
     * @param o the object to compare this Operation against
     * @return true if the given object represents an Operation with the same ID
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Operation other = (Operation) o;
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