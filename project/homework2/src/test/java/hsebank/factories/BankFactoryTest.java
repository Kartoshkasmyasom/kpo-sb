package hsebank.factories;

import static org.junit.jupiter.api.Assertions.*;

import hsebank.domains.BankAccount;
import hsebank.domains.Category;
import hsebank.domains.Operation;
import hsebank.enums.CategoryType;
import hsebank.enums.OperationType;
import hsebank.params.AccountCreationParams;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class BankFactoryTest {

  private final BankFactory factory = new BankFactory();

  @Test
  void createBankAccountValid() {
    AccountCreationParams params =
        new AccountCreationParams.Builder("  User  ").balance(1000).build();

    BankAccount acc = factory.createBankAccount(params);

    assertEquals("User", acc.getName());
    assertEquals(1000, acc.getBalance());
  }

  @Test
  void createCategoryValid() {
    Category cat = factory.createCategory(CategoryType.INCOME, "  Salary ");

    assertEquals(CategoryType.INCOME, cat.getType());
    assertEquals("Salary", cat.getName());
  }

  @Test
  void createCategoryNullTypeThrows() {
    assertThrows(IllegalArgumentException.class, () -> factory.createCategory(null, "Name"));
  }

  @Test
  void createCategoryEmptyNameThrows() {
    assertThrows(
        IllegalArgumentException.class, () -> factory.createCategory(CategoryType.EXPENSE, "   "));
  }

  @Test
  void createOperationValid() {
    LocalDate date = LocalDate.now().minusDays(1);

    Operation op = factory.createOperation(OperationType.INCOME, 1, 2, 500, date);

    assertEquals(OperationType.INCOME, op.getType());
    assertEquals(1, op.getBankAccountId());
    assertEquals(2, op.getCategoryId());
    assertEquals(500, op.getAmount());
    assertEquals(date, op.getDate());
  }

  @Test
  void createOperationNegativeAmountThrows() {
    assertThrows(
        IllegalArgumentException.class,
        () -> factory.createOperation(OperationType.EXPENSE, 1, 2, -10, LocalDate.now()));
  }

  @Test
  void createOperationNullDateThrows() {
    assertThrows(
        IllegalArgumentException.class,
        () -> factory.createOperation(OperationType.INCOME, 1, 2, 10, null));
  }

  @Test
  void createOperationFutureDateThrows() {
    LocalDate future = LocalDate.now().plusDays(1);
    assertThrows(
        IllegalArgumentException.class,
        () -> factory.createOperation(OperationType.INCOME, 1, 2, 10, future));
  }

  @Test
  void createOperationTooOldDateThrows() {
    LocalDate old = LocalDate.now().minusYears(11);
    assertThrows(
        IllegalArgumentException.class,
        () -> factory.createOperation(OperationType.INCOME, 1, 2, 10, old));
  }
}
