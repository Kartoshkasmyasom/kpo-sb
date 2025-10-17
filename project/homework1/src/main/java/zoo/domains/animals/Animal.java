package zoo.domains.animals;

import lombok.Getter;
import lombok.Setter;
import zoo.interfaces.IAlive;
import zoo.interfaces.IInventory;

/**
 * Animal class.
 */
public abstract class Animal implements IAlive, IInventory {
    protected int food = 0;
    @Setter
    @Getter
    protected int number = 0;
    @Getter
    protected String name;

    public Animal(String name) {
        this.name = name;
    }

    public Animal(String name, int food) {
        this.name = name;
        this.food = food;
    }

    /**
     * Animal constructor.
     *
     * @param name   name of animal
     * @param food   daily food of animal
     * @param number number of animal
     */
    public Animal(String name, int food, int number) {
        this.name = name;
        this.food = food;
        this.number = number;
    }

    public abstract boolean isContactable();

    public void setDailyFoodIntake(int food) {
        this.food = food;
    }

    public int getDailyFoodIntake() {
        return food;
    }
}