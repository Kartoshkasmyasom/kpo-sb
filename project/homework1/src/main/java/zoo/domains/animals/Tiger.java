package zoo.domains.animals;

/**
 * Tiger animal class.
 */
public class Tiger extends Predator {

    public Tiger(String name, int food) {
        super(name, food);
    }

    public Tiger(String name, int food, int number) {
        super(name, food, number);
    }

    public Tiger(String name) {
        super(name);
    }
}