package zoo.services;

import org.junit.jupiter.api.Test;
import zoo.domains.animals.Monkey;
import zoo.domains.animals.Tiger;

import static org.junit.jupiter.api.Assertions.*;

class VclinicTest {

    @Test
    void testVclinicAcceptsAllAnimals() {
        Vclinic vclinic = new Vclinic();
        Monkey monkey = new Monkey("TestMonkey", 5);
        Tiger tiger = new Tiger("TestTiger", 10);

        assertTrue(vclinic.acceptedIntoZoo(monkey));
        assertTrue(vclinic.acceptedIntoZoo(tiger));
    }
}