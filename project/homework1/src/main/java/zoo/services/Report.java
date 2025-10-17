package zoo.services;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Setter;
import zoo.domains.animals.Animal;
import zoo.interfaces.IReport;

/**
 * Report on number of animals, daily food intake and inventory.
 */
public class Report implements IReport {
    @Setter
    private Zoo zoo;

    @Override
    public List<Animal> listAnimals() {
        return zoo.getAnimals();
    }

    @Override
    public String contactAnimals() {
        return zoo.getAnimals().stream()
                .filter(Animal::isContactable)
                .map(animal -> animal.getName() + " (№" + animal.getNumber() + ")")
                .collect(Collectors.joining(", "));
    }

    @Override
    public String getInventory() {
        return zoo.getInventories().stream()
                .map(inv -> inv.getClass().getSimpleName() + " (№" + inv.getNumber() + ")")
                .collect(Collectors.joining(", "));
    }

    @Override
    public int animalsTotal() {
        return zoo.getAnimals().size();
    }

    @Override
    public int foodTotal() {
        return zoo.getAnimals().stream()
                .mapToInt(Animal::getDailyFoodIntake)
                .sum();
    }
}