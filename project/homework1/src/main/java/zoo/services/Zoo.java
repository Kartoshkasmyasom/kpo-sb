package zoo.services;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import zoo.domains.animals.Animal;
import zoo.domains.objects.Thing;
import zoo.interfaces.IInventory;

/**
 * Main zoo class managing animals and inventory.
 */
public class Zoo {
    @Getter
    private List<Animal> animals;
    @Getter
    private List<IInventory> inventories;
    private final Vclinic vclinic;

    /**
     * Creates new zoo with clinic.
     */
    public Zoo(Vclinic vclinic) {
        this.vclinic = vclinic;
        this.animals = new ArrayList<>();
        this.inventories = new ArrayList<>();
    }

    /**
     * Adds animal if healthy.
     *
     * @return true if animal added
     */
    public boolean addAnimal(Animal animal) {
        if (!vclinic.acceptedIntoZoo(animal)) {
            return false;
        }
        animals.add(animal);
        inventories.add(animal);
        return true;
    }

    public void addThing(Thing thing) {
        inventories.add(thing);
    }
}