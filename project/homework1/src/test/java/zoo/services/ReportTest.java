package zoo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zoo.domains.animals.*;
import zoo.domains.objects.Computer;
import zoo.domains.objects.Table;

import static org.junit.jupiter.api.Assertions.*;

class ReportTest {

    private Zoo zoo;
    private Report report;
    private Vclinic vclinic;

    @BeforeEach
    void setUp() {
        vclinic = new Vclinic();
        zoo = new Zoo(vclinic);
        report = new Report();
        report.setZoo(zoo);
    }

    @Test
    void testEmptyZoo() {
        assertEquals(0, report.animalsTotal());
        assertEquals(0, report.foodTotal());
        assertEquals("", report.contactAnimals());
        assertTrue(report.listAnimals().isEmpty());
    }

    @Test
    void testListAnimals() {
        zoo.addAnimal(new Monkey("Monkey1", 5, 1, 7));
        zoo.addAnimal(new Tiger("Tiger1", 10, 2));

        assertEquals(2, report.listAnimals().size());
        assertEquals("Monkey1", report.listAnimals().get(0).getName());
        assertEquals("Tiger1", report.listAnimals().get(1).getName());
    }

    @Test
    void testContactAnimalsWithMixedTypes() {
        zoo.addAnimal(new Monkey("FriendlyMonkey", 5, 1, 8)); // contactable
        zoo.addAnimal(new Rabbit("ShyRabbit", 2, 2, 3)); // not contactable
        zoo.addAnimal(new Tiger("Tiger", 10, 3)); // not contactable

        String contacts = report.contactAnimals();
        assertTrue(contacts.contains("FriendlyMonkey"));
        assertFalse(contacts.contains("ShyRabbit"));
        assertFalse(contacts.contains("Tiger"));
    }

    @Test
    void testFoodTotalCalculation() {
        zoo.addAnimal(new Monkey("Monkey1", 5, 1, 7));
        zoo.addAnimal(new Rabbit("Rabbit1", 3, 2, 6));
        zoo.addAnimal(new Tiger("Tiger1", 12, 3));

        assertEquals(20, report.foodTotal()); // 5 + 3 + 12
    }

    @Test
    void testGetInventoryMixed() {
        zoo.addAnimal(new Monkey("Monkey", 5, 1, 7));
        zoo.addThing(new Table(2));
        zoo.addThing(new Computer(3));

        String inventory = report.getInventory();
        assertTrue(inventory.contains("Monkey"));
        assertTrue(inventory.contains("Table"));
        assertTrue(inventory.contains("Computer"));
    }

    @Test
    void testAllContactableAnimals() {
        zoo.addAnimal(new Monkey("Monkey1", 3, 1, 9));
        zoo.addAnimal(new Rabbit("Rabbit1", 2, 2, 8));
        zoo.addAnimal(new Rabbit("Rabbit2", 2, 3, 7));

        String contacts = report.contactAnimals();
        assertTrue(contacts.contains("Monkey1"));
        assertTrue(contacts.contains("Rabbit1"));
        assertTrue(contacts.contains("Rabbit2"));
    }

    @Test
    void testNoContactableAnimals() {
        zoo.addAnimal(new Rabbit("Rabbit1", 2, 1, 3));
        zoo.addAnimal(new Rabbit("Rabbit2", 2, 2, 4));
        zoo.addAnimal(new Tiger("Tiger", 10, 3));

        String contacts = report.contactAnimals();
        assertTrue(contacts.isEmpty());
    }
}