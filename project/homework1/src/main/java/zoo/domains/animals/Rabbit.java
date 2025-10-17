package zoo.domains.animals;

/**
 * Rabbit animal class.
 */
public class Rabbit extends Herbo {

    public Rabbit(String name, int kindness) {
        super(name, kindness);
    }

    public Rabbit(String name, int food, int kindness) {
        super(name, food, kindness);
    }

    public Rabbit(String name, int food, int number, int kindness) {
        super(name, food, number, kindness);
    }
}