package zoo.domains.animals;

/**
 * Wolf animal class.
 */
public class Wolf extends Predator {

    public Wolf(String name) {
        super(name);
    }

    public Wolf(String name, int food) {
        super(name, food);
    }

    public Wolf(String name, int food, int number) {
        super(name, food, number);
    }
}