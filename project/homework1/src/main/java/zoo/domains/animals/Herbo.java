package zoo.domains.animals;

import zoo.interfaces.IHerbo;

/**
 * Herbivorous animal class.
 */
public class Herbo extends Animal implements IHerbo {
    protected int kindness;

    protected boolean isRangeValid(int kindness) {
        return 1 <= kindness && kindness <= 10;
    }

    public Herbo(String name, int kindness) {
        super(name);
        setKindnessLevel(kindness);
    }

    @Override
    public boolean isContactable() {
        return kindness > 5;
    }

    public Herbo(String name, int food, int kindness) {
        super(name, food);
        setKindnessLevel(kindness);
    }

    public Herbo(String name, int food, int number, int kindness) {
        super(name, food, number);
        setKindnessLevel(kindness);
    }

    /**
     * Sets kindness level (1-10).
     */
    public void setKindnessLevel(int kindness) {
        if (!isRangeValid(kindness)) {
            throw new IllegalArgumentException("Error: kindness level should be an integer from 1 to 10!");
        }
        this.kindness = kindness;
    }

    public int getKindnessLevel() {
        return kindness;
    }
}