package zoo.domains.animals;

/**
 * Monkey animal class.
 */
public class Monkey extends Herbo {

    public Monkey(String name, int kindness) {
        super(name, kindness);
    }

    public Monkey(String name, int food, int kindness) {
        super(name, food, kindness);
    }

    public Monkey(String name, int food, int number, int kindness) {
        super(name, food, number, kindness);
    }
}