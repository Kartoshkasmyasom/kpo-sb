package zoo.domains.animals;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    void testAnimalCreation() {
        Monkey monkey = new Monkey("TestMonkey", 5);
        assertEquals("TestMonkey", monkey.getName());
        assertEquals(0, monkey.getDailyFoodIntake());
        assertEquals(0, monkey.getNumber());
    }

    @Test
    void testAnimalWithFood() {
        Monkey monkey = new Monkey("TestMonkey", 3, 5);
        assertEquals("TestMonkey", monkey.getName());
        assertEquals(3, monkey.getDailyFoodIntake());
        assertEquals(5, monkey.getKindnessLevel());
    }

    @Test
    void testAnimalWithNumber() {
        Monkey monkey = new Monkey("TestMonkey", 2, 1, 6);
        assertEquals("TestMonkey", monkey.getName());
        assertEquals(2, monkey.getDailyFoodIntake());
        assertEquals(1, monkey.getNumber());
        assertEquals(6, monkey.getKindnessLevel());
    }

    @Test
    void testSetDailyFoodIntake() {
        Monkey monkey = new Monkey("TestMonkey", 5);
        monkey.setDailyFoodIntake(10);
        assertEquals(10, monkey.getDailyFoodIntake());
    }

    @Test
    void testSetNumber() {
        Monkey monkey = new Monkey("TestMonkey", 5);
        monkey.setNumber(5);
        assertEquals(5, monkey.getNumber());
    }
}