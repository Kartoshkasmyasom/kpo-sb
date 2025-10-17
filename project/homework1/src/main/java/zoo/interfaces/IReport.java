package zoo.interfaces;

import java.util.List;
import zoo.domains.animals.Animal;

/**
 * Report interface for zoo statistics.
 */
public interface IReport {
    public List<Animal> listAnimals();

    public String contactAnimals();

    public String getInventory();

    public int animalsTotal();

    public int foodTotal();
}
