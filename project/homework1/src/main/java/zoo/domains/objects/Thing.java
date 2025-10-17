package zoo.domains.objects;

import lombok.Getter;
import zoo.interfaces.IInventory;

/**
 * Base thing class for object items.
 */
@Getter
public class Thing implements IInventory {
    protected int number = 0;

    public Thing(int number) {
        setNumber(number);
    }

    /**
     * Sets inventory number.
     */
    public void setNumber(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Error: number should be a countable non-negative!");
        }
        this.number = number;
    }

}