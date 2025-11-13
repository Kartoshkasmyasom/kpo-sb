package hsebank.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import hsebank.domains.BankAccount;
import hsebank.domains.Category;
import hsebank.enums.CategoryType;
import hsebank.facades.BankAccountFacade;
import hsebank.facades.CategoryFacade;
import hsebank.facades.OperationFacade;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ApplicationControllerTest {

  private final PrintStream originalOut = System.out;
  private final InputStream originalIn = System.in;

  @AfterEach
  void restoreSystemIO() {
    System.setOut(originalOut);
    System.setIn(originalIn);
  }

  private String runWithInput(
      String input,
      BankAccountFacade accountFacade,
      CategoryFacade categoryFacade,
      OperationFacade operationFacade) {
    // сначала подменяем System.in
    ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    System.setIn(in);

    // потом подменяем System.out
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));

    // создаём контроллер уже после подмены System.in
    ApplicationController controller =
        new ApplicationController(accountFacade, categoryFacade, operationFacade);

    controller.run();

    return outContent.toString(StandardCharsets.UTF_8);
  }

  @Test
  void exitImmediatelyOnZero() {
    BankAccountFacade acc = mock(BankAccountFacade.class);
    CategoryFacade cat = mock(CategoryFacade.class);
    OperationFacade op = mock(OperationFacade.class);

    String output = runWithInput("0\n", acc, cat, op);

    // проверяем, что хотя бы меню напечаталось
    assertTrue(output.contains("1 acc:create"), "menu should be printed");

    // никаких вызовов фасадов
    verifyNoInteractions(acc, cat, op);
  }

  @Test
  void createAccountHappyPath() {
    BankAccountFacade acc = mock(BankAccountFacade.class);
    CategoryFacade cat = mock(CategoryFacade.class);
    OperationFacade op = mock(OperationFacade.class);

    // подготавливаем возврат из фасада
    when(acc.create(eq("Magical meow"), eq(1000)))
        .thenReturn(new BankAccount("Magical meow", 1000));

    // сценарий: 1 (create), имя, баланс, 0 (exit)
    String output = runWithInput("1\nMagical meow\n1000\n0\n", acc, cat, op);

    // проверяем, что фасад вызван с нужными параметрами
    verify(acc, times(1)).create("Magical meow", 1000);

    // программа не должна упасть, меню показывает хотя бы два раза
    assertTrue(output.contains("1 acc:create"));
  }

  @Test
  void listAccountsPrintsAccounts() {
    BankAccountFacade acc = mock(BankAccountFacade.class);
    CategoryFacade cat = mock(CategoryFacade.class);
    OperationFacade op = mock(OperationFacade.class);

    BankAccount a = new BankAccount("UserA", 42);
    when(acc.list_all_accounts()).thenReturn(List.of(a));

    // 4 (list), затем 0 (exit)
    String output = runWithInput("4\n0\n", acc, cat, op);

    // verify вызов
    verify(acc, times(1)).list_all_accounts();

    // проверяем, что объект напечатан контроллером
    assertTrue(output.contains("name: UserA"), "controller should print account name");
    assertTrue(output.contains("balance: 42"), "controller should print account balance");
  }

  @Test
  void safeParseIntHandlesInvalidInputThenValid() {
    BankAccountFacade acc = mock(BankAccountFacade.class);
    CategoryFacade cat = mock(CategoryFacade.class);
    OperationFacade op = mock(OperationFacade.class);

    when(acc.create(eq("User"), eq(100))).thenReturn(new BankAccount("User", 100));

    // сценарий:
    // 1 (create)
    // name -> "User"
    // balance -> "abc" (ошибка), потом "100"
    // 0 (exit)
    String input = String.join("\n", "1", "User", "abc", "100", "0") + "\n";

    String output = runWithInput(input, acc, cat, op);

    // проверяем, что сначала было сообщение об ошибке
    assertTrue(output.contains("Error: enter correct number!"));

    // и всё-таки в итоге вызвали create с 100
    verify(acc, times(1)).create("User", 100);
  }

  @Test
  void safeParseCategoryTypeHandlesInvalidThenValid() {
    BankAccountFacade acc = mock(BankAccountFacade.class);
    CategoryFacade cat = mock(CategoryFacade.class);
    OperationFacade op = mock(OperationFacade.class);

    Category c = new Category(CategoryType.INCOME, "Salary");
    when(cat.create(eq(CategoryType.INCOME), eq("Salary"))).thenReturn(c);

    // 5 (cat:create)
    // type -> "wrong" (ошибка), потом "income"
    // name -> "Salary"
    // 0 (exit)
    String input = String.join("\n", "5", "wrong", "income", "Salary", "0") + "\n";

    String output = runWithInput(input, acc, cat, op);

    assertTrue(output.contains("Error: Category type can only be INCOME or EXPENSE!"));

    verify(cat, times(1)).create(CategoryType.INCOME, "Salary");
  }
}
