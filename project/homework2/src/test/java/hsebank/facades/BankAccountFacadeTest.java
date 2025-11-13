package hsebank.facades;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hsebank.domains.BankAccount;
import hsebank.factories.BankFactory;
import hsebank.interfaces.IRepository;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankAccountFacadeTest {

  private IRepository<BankAccount> repo;
  private BankFactory factory;
  private BankAccountFacade facade;

  @BeforeEach
  void setUp() {
    repo = mock(IRepository.class);
    factory = spy(new BankFactory());
    facade = new BankAccountFacade(repo, factory);
  }

  @Test
  void create_validData_persistsAccount() {
    // arrange
    String name = "Test user";
    int balance = 1000;

    // act
    BankAccount acc = facade.create(name, balance);

    // assert
    assertNotNull(acc);
    assertEquals(name, acc.getName());
    assertEquals(balance, acc.getBalance());
    verify(repo, times(1)).save(acc);
  }

  @Test
  void create_invalidName_throwsException() {
    assertThrows(Exception.class, () -> facade.create("  ", 100));
  }

  @Test
  void rename_valid_renamesAndSaves() {
    BankAccount acc = new BankAccount("Old", 100);
    when(repo.findById(acc.getId())).thenReturn(Optional.of(acc));

    facade.rename(acc.getId(), "  New Name  ");

    assertEquals("New Name", acc.getName());
    verify(repo).save(acc);
  }

  @Test
  void rename_missingAccount_throws() {
    when(repo.findById(42)).thenReturn(Optional.empty());
    assertThrows(IllegalArgumentException.class, () -> facade.rename(42, "Name"));
  }

  @Test
  void deposit_positiveAmount_increasesBalance() {
    BankAccount acc = new BankAccount("User", 100);
    when(repo.findById(acc.getId())).thenReturn(Optional.of(acc));

    facade.deposit(acc.getId(), 50);

    assertEquals(150, acc.getBalance());
    verify(repo).save(acc);
  }

  @Test
  void deposit_negativeAmount_throws() {
    BankAccount acc = new BankAccount("User", 100);
    when(repo.findById(acc.getId())).thenReturn(Optional.of(acc));

    assertThrows(IllegalArgumentException.class, () -> facade.deposit(acc.getId(), -10));
  }

  @Test
  void withdraw_valid_decreasesBalance() {
    BankAccount acc = new BankAccount("User", 100);
    when(repo.findById(acc.getId())).thenReturn(Optional.of(acc));

    facade.withdraw(acc.getId(), 40);

    assertEquals(60, acc.getBalance());
    verify(repo).save(acc);
  }

  @Test
  void withdraw_notEnoughFunds_throws() {
    BankAccount acc = new BankAccount("User", 30);
    when(repo.findById(acc.getId())).thenReturn(Optional.of(acc));

    assertThrows(IllegalStateException.class, () -> facade.withdraw(acc.getId(), 100));
  }
}
