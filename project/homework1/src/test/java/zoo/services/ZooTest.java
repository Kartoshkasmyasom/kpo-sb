package zoo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zoo.domains.animals.Monkey;
import zoo.domains.objects.Table;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ZooTest {

    @Mock
    private Vclinic vclinic;

    private Zoo zoo;

    @BeforeEach
    void setUp() {
        zoo = new Zoo(vclinic);
    }

    @Test
    void testAddAnimal_Success() {
        when(vclinic.acceptedIntoZoo(any())).thenReturn(true);
        Monkey monkey = new Monkey("Тест", 5);
        boolean result = zoo.addAnimal(monkey);

        assertTrue(result);
        assertEquals(1, zoo.getAnimals().size());
        assertEquals(1, zoo.getInventories().size());
    }

    @Test
    void testAddAnimal_RejectedByClinic() {
        when(vclinic.acceptedIntoZoo(any())).thenReturn(false);
        Monkey monkey = new Monkey("Тест", 5);

        boolean result = zoo.addAnimal(monkey);

        assertFalse(result);
        assertEquals(0, zoo.getAnimals().size());
    }

    @Test
    void testAddThing() {
        Table table = new Table(1);
        zoo.addThing(table);

        assertEquals(1, zoo.getInventories().size());
    }
}