package zoo;

import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zoo.domains.animals.Monkey;
import zoo.domains.animals.Rabbit;
import zoo.domains.animals.Tiger;
import zoo.domains.objects.Computer;
import zoo.domains.objects.Table;
import zoo.services.Report;
import zoo.services.Zoo;

/**
 * ZooApplication class.
 */
@SpringBootApplication
public class ZooApplication implements CommandLineRunner {

    @Autowired
    private Zoo zoo;

    @Autowired
    private Report report;

    public static void main(String[] args) {
        SpringApplication.run(ZooApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Добавляем тестовые данные
        initializeTestData();

        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addAnimal(scanner);
                    break;
                case "2":
                    showAnimalList();
                    break;
                case "3":
                    showContactAnimals();
                    break;
                case "4":
                    showFoodTotal();
                    break;
                case "5":
                    showInventory();
                    break;
                case "6":
                    showStatistics();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Неверный выбор!");
            }
        }
        System.out.println("До свидания!");
    }

    private void printMenu() {
        System.out.println("\n=== МОСКОВСКИЙ ЗООПАРК ===");
        System.out.println("1. Добавить животное");
        System.out.println("2. Список животных");
        System.out.println("3. Контактные животные");
        System.out.println("4. Общее количество еды");
        System.out.println("5. Инвентарь");
        System.out.println("6. Статистика");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private void initializeTestData() {
        // Добавляем тестовых животных
        zoo.addAnimal(new Monkey("Мартышка", 5, 1, 7));
        zoo.addAnimal(new Rabbit("Кролик", 2, 2, 8));
        zoo.addAnimal(new Tiger("Тигр", 10, 3));

        // Добавляем инвентарь
        zoo.addThing(new Table(4));
        zoo.addThing(new Computer(5));
    }

    private void addAnimal(Scanner scanner) {
        System.out.println("\n--- Добавление животного ---");
        // Реализация диалога добавления животного
        System.out.println("Функция в разработке...");
    }

    private void showAnimalList() {
        System.out.println("\n--- Список животных ---");
        report.listAnimals().forEach(animal ->
                System.out.println(animal.getName() + " - Еда: "
                        + animal.getDailyFoodIntake() + "кг, №" + animal.getNumber()));
    }

    private void showContactAnimals() {
        System.out.println("\n--- Контактные животные ---");
        String contacts = report.contactAnimals();
        System.out.println(contacts.isEmpty() ? "Нет контактных животных" : contacts);
    }

    private void showFoodTotal() {
        System.out.println("\n--- Общее количество еды ---");
        System.out.println("Всего еды требуется: " + report.foodTotal() + "кг/день");
    }

    private void showInventory() {
        System.out.println("\n--- Инвентарь ---");
        System.out.println(report.getInventory());
    }

    private void showStatistics() {
        System.out.println("\n--- Статистика ---");
        System.out.println("Всего животных: " + report.animalsTotal());
        System.out.println("Всего еды: " + report.foodTotal() + "кг/день");
        System.out.println("Контактные животные: "
                + (report.contactAnimals().isEmpty() ? "нет" : "есть"));
    }
}