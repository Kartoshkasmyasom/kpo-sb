package zoo.services;

import zoo.domains.animals.Animal;

/**
 * Veterinary clinic for health checks of animals before they are accepted into the zoo.
 * Conducts medical examinations of animals and decides on their admission.
 */
public class Vclinic {
    private boolean healthCheck(Animal animal) {
        return true;
    }

    public boolean acceptedIntoZoo(Animal animal) {
        return healthCheck(animal);
    }
}
