package zoo.domains.animals;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PredatorTest {

    @Test
    void testPredatorCreation() {
        Tiger tiger = new Tiger("Shere Khan", 15);
        assertEquals("Shere Khan", tiger.getName());
        assertEquals(15, tiger.getDailyFoodIntake());
        assertFalse(tiger.isContactable());
    }

    @Test
    void testWolfCreation() {
        Wolf wolf = new Wolf("Grey", 8, 4);
        assertEquals("Grey", wolf.getName());
        assertEquals(8, wolf.getDailyFoodIntake());
        assertEquals(4, wolf.getNumber());
        assertFalse(wolf.isContactable());
    }

    @Test
    void testPredatorNeverContactable() {
        Tiger tiger = new Tiger("TestTiger", 10);
        Wolf wolf = new Wolf("TestWolf", 5);

        assertFalse(tiger.isContactable());
        assertFalse(wolf.isContactable());
    }
}