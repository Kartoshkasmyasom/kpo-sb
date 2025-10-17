package zoo.interfaces;

/**
 * Interface for herbivorous animals.
 */
public interface IHerbo extends IAlive {
    void setKindnessLevel(int kindness);

    int getKindnessLevel();
}