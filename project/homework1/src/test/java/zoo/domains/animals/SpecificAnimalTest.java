package zoo.domains.animals;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecificAnimalTest {

    @Test
    void testMonkeyCreation() {
        Monkey monkey = new Monkey("George", 6);
        assertEquals("George", monkey.getName());
        assertEquals(6, monkey.getKindnessLevel());
        assertTrue(monkey.isContactable());
    }

    @Test
    void testRabbitCreation() {
        Rabbit rabbit = new Rabbit("Bugs", 2, 4, 9);
        assertEquals("Bugs", rabbit.getName());
        assertEquals(2, rabbit.getDailyFoodIntake());
        assertEquals(4, rabbit.getNumber());
        assertEquals(9, rabbit.getKindnessLevel());
        assertTrue(rabbit.isContactable());
    }

    @Test
    void testTigerCreation() {
        Tiger tiger = new Tiger("Richard Parker", 15, 5);
        assertEquals("Richard Parker", tiger.getName());
        assertEquals(15, tiger.getDailyFoodIntake());
        assertEquals(5, tiger.getNumber());
        assertFalse(tiger.isContactable());
    }

    @Test
    void testWolfCreation() {
        Wolf wolf = new Wolf("Ghost", 8);
        assertEquals("Ghost", wolf.getName());
        assertEquals(8, wolf.getDailyFoodIntake());
        assertFalse(wolf.isContactable());
    }
}