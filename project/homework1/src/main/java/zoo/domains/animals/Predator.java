package zoo.domains.animals;

import zoo.interfaces.IPredator;

/**
 * Predatory animal class.
 */
public class Predator extends Animal implements IPredator {

    public Predator(String name) {
        super(name);
    }

    public Predator(String name, int food) {
        super(name, food);
    }

    public Predator(String name, int food, int number) {
        super(name, food, number);
    }

    @Override
    public boolean isContactable() {
        return false;
    }
}