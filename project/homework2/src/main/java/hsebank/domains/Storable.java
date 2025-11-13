package hsebank.domains;

import hsebank.interfaces.IStorable;
import java.util.Random;
import lombok.Getter;

/**
 * Base class for all storable entities in the system.
 * Provides automatic ID generation and implements the IStorable interface.
 */
public class Storable implements IStorable {
    private static final Random random = new Random();

    @Getter
    protected final int id;

    /**
     * Constructs a new Storable instance with a randomly generated ID.
     * The ID is generated in the range from 1 to Integer.MAX_VALUE.
     */
    protected Storable() {
        this.id = random.nextInt(Integer.MAX_VALUE) + 1;
    }
}