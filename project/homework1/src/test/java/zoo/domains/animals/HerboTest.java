package zoo.domains.animals;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HerboTest {

    @Test
    void testHerboCreation() {
        Rabbit rabbit = new Rabbit("Bunny", 7);
        assertEquals("Bunny", rabbit.getName());
        assertEquals(7, rabbit.getKindnessLevel());
        assertTrue(rabbit.isContactable());
    }

    @Test
    void testHerboContactable() {
        Rabbit highKindness = new Rabbit("Friendly", 8);
        Rabbit lowKindness = new Rabbit("Shy", 3);

        assertTrue(highKindness.isContactable());
        assertFalse(lowKindness.isContactable());
    }

    @Test
    void testSetValidKindnessLevel() {
        Rabbit rabbit = new Rabbit("Bunny", 5);
        rabbit.setKindnessLevel(10);
        assertEquals(10, rabbit.getKindnessLevel());
    }

    @Test
    void testSetInvalidKindnessLevel() {
        Rabbit rabbit = new Rabbit("Bunny", 5);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            rabbit.setKindnessLevel(11);
        });

        assertEquals("Error: kindness level should be an integer from 1 to 10!", exception.getMessage());
    }

    @Test
    void testSetZeroKindnessLevel() {
        Rabbit rabbit = new Rabbit("Bunny", 5);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            rabbit.setKindnessLevel(0);
        });

        assertEquals("Error: kindness level should be an integer from 1 to 10!", exception.getMessage());
    }
}