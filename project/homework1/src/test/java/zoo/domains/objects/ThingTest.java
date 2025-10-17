package zoo.domains.objects;

import org.junit.jupiter.api.Test;
import zoo.interfaces.IInventory;

import static org.junit.jupiter.api.Assertions.*;

class ThingTest {

    @Test
    void testThingCreation() {
        Table table = new Table(1);
        assertEquals(1, table.getNumber());
    }

    @Test
    void testComputerCreation() {
        Computer computer = new Computer(2);
        assertEquals(2, computer.getNumber());
    }

    @Test
    void testSetValidNumber() {
        Table table = new Table(1);
        table.setNumber(5);
        assertEquals(5, table.getNumber());
    }

    @Test
    void testSetInvalidNumber() {
        Table table = new Table(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            table.setNumber(-1);
        });

        assertEquals("Error: number should be a countable non-negative!", exception.getMessage());
    }

    @Test
    void testThingImplementsIInventory() {
        Table table = new Table(1);
        assertTrue(table instanceof IInventory);
    }
}