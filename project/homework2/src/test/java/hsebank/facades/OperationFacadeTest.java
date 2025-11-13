package hsebank.facades;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hsebank.domains.BankAccount;
import hsebank.domains.Category;
import hsebank.domains.Operation;
import hsebank.enums.CategoryType;
import hsebank.enums.OperationType;
import hsebank.factories.BankFactory;
import hsebank.interfaces.IRepository;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OperationFacadeTest {

  private IRepository<Operation> opRepo;
  private BankAccountFacade accountFacade;
  private CategoryFacade categoryFacade;
  private BankFactory factory;
  private OperationFacade facade;

  @BeforeEach
  void setUp() {
    opRepo = mock(IRepository.class);
    accountFacade = mock(BankAccountFacade.class);
    categoryFacade = mock(CategoryFacade.class);
    factory = new BankFactory();
    facade = new OperationFacade(opRepo, accountFacade, categoryFacade, factory);
  }

  @Test
  void create_incomeOperation_updatesAccountBalance() {
    BankAccount acc = new BankAccount("User", 100);
    Category cat = new Category(CategoryType.INCOME, "Salary");

    when(accountFacade.get(acc.getId())).thenReturn(Optional.of(acc));
    when(categoryFacade.get(cat.getId())).thenReturn(Optional.of(cat));

    Operation op =
        facade.create(OperationType.INCOME, acc.getId(), cat.getId(), 200, LocalDate.now());

    assertNotNull(op);
    verify(opRepo).save(op);
    verify(accountFacade).deposit(acc.getId(), 200);
  }

  @Test
  void create_mismatchedTypes_throws() {
    BankAccount acc = new BankAccount("User", 100);
    Category cat = new Category(CategoryType.EXPENSE, "Food");

    when(accountFacade.get(acc.getId())).thenReturn(Optional.of(acc));
    when(categoryFacade.get(cat.getId())).thenReturn(Optional.of(cat));

    assertThrows(
        IllegalArgumentException.class,
        () -> facade.create(OperationType.INCOME, acc.getId(), cat.getId(), 50, LocalDate.now()));
  }

  @Test
  void delete_operation_callsRepo() {
    facade.delete(123);
    verify(opRepo).delete(123);
  }
}
