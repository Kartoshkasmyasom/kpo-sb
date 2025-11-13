package hsebank.client.commands;

import static org.junit.jupiter.api.Assertions.*;

import hsebank.domains.BankAccount;
import hsebank.domains.Category;
import hsebank.domains.Operation;
import hsebank.domains.Storable;
import hsebank.enums.CategoryType;
import hsebank.facades.BankAccountFacade;
import hsebank.facades.CategoryFacade;
import hsebank.facades.OperationFacade;
import hsebank.factories.BankFactory;
import hsebank.interfaces.IRepository;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandsTest {

  private static class InMemoryRepository<T extends Storable> implements IRepository<T> {
    private final List<T> data = new CopyOnWriteArrayList<>();

    @Override
    public List<T> findAll() {
      return new ArrayList<>(data);
    }

    @Override
    public Optional<T> findById(int id) {
      return data.stream().filter(x -> x.getId() == id).findFirst();
    }

    @Override
    public void save(T object) {
      data.removeIf(x -> x.getId() == object.getId());
      data.add(object);
    }

    @Override
    public void delete(int id) {
      data.removeIf(x -> x.getId() == id);
    }

    @Override
    public void clear() {
      var list = findAll();
      for (var obj : list) {
        delete(obj.getId());
      }
    }
  }

  private BankAccountFacade accountFacade;
  private CategoryFacade categoryFacade;
  private OperationFacade operationFacade;

  @BeforeEach
  void setUp() {
    BankFactory factory = new BankFactory();

    IRepository<BankAccount> accRepo = new InMemoryRepository<>();
    IRepository<Category> catRepo = new InMemoryRepository<>();
    IRepository<Operation> opRepo = new InMemoryRepository<>();

    accountFacade = new BankAccountFacade(accRepo, factory);
    categoryFacade = new CategoryFacade(catRepo, factory);
    operationFacade = new OperationFacade(opRepo, accountFacade, categoryFacade, factory);
  }

  @Test
  void createAndRenameAndDeleteAccountViaCommands() {
    // create
    CreateAccountCommand create = new CreateAccountCommand(accountFacade, "User", 1000);
    BankAccount acc = create.execute();
    assertNotNull(acc);
    assertEquals("User", acc.getName());

    int id = acc.getId();

    // rename
    RenameAccountCommand rename = new RenameAccountCommand(accountFacade, id, "NewName");
    rename.execute();
    BankAccount renamed = accountFacade.get(id).orElseThrow();
    assertEquals("NewName", renamed.getName());

    // delete
    DeleteAccountCommand del = new DeleteAccountCommand(accountFacade, id);
    del.execute();
    assertTrue(accountFacade.get(id).isEmpty());
  }

  @Test
  void listAccountsCommandReturnsList() {
    accountFacade.create("A", 10);
    accountFacade.create("B", 20);

    ListAccountsCommand cmd = new ListAccountsCommand(accountFacade);
    var list = cmd.execute();

    assertEquals(2, list.size());
  }

  @Test
  void categoryCommandsWork() {
    CreateCategoryCommand createCat =
        new CreateCategoryCommand(categoryFacade, CategoryType.INCOME, "Salary");
    Category c = createCat.execute();
    assertNotNull(c);
    int id = c.getId();

    RenameCategoryCommand renameCat = new RenameCategoryCommand(categoryFacade, id, "Main Salary");
    renameCat.execute();
    assertEquals("Main Salary", categoryFacade.get(id).orElseThrow().getName());

    ListCategoriesCommand listCats = new ListCategoriesCommand(categoryFacade);
    assertFalse(listCats.execute().isEmpty());

    DeleteCategoryCommand delCat = new DeleteCategoryCommand(categoryFacade, id);
    delCat.execute();
    assertTrue(categoryFacade.get(id).isEmpty());
  }

  @Test
  void operationCommandsDepositAndWithdraw() {
    BankAccount acc = accountFacade.create("User", 100);
    Category incomeCat = categoryFacade.create(CategoryType.INCOME, "Salary");
    Category expenseCat = categoryFacade.create(CategoryType.EXPENSE, "Food");

    DepositCommand dep = new DepositCommand(operationFacade, acc.getId(), incomeCat.getId(), 200);
    dep.execute();

    WithdrawCommand w = new WithdrawCommand(operationFacade, acc.getId(), expenseCat.getId(), 50);
    w.execute();

    BankAccount updated = accountFacade.get(acc.getId()).orElseThrow();
    assertEquals(250, updated.getBalance());
  }
}
