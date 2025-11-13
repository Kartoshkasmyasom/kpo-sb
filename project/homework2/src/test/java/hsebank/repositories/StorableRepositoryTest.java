package hsebank.repositories;

import static org.junit.jupiter.api.Assertions.*;

import hsebank.domains.BankAccount;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class StorableRepositoryTest {

  @TempDir Path tempDir;

  @Test
  void saveAndFindAll_persistsData() {
    StorableRepository<BankAccount> repo =
        new StorableRepository<>("accounts_test1.json", BankAccount.class);
    repo.clear();

    BankAccount a1 = new BankAccount("User1", 100);
    BankAccount a2 = new BankAccount("User2", 200);

    repo.save(a1);
    repo.save(a2);

    List<BankAccount> all = repo.findAll();
    assertEquals(2, all.size());
  }

  @Test
  void delete_removesById() {
    Path testDbPath = tempDir.resolve("accounts_test2.json");
    StorableRepository<BankAccount> repo =
        new StorableRepository<>("accounts_test2.json", BankAccount.class);

    repo.clear();
    BankAccount a1 = new BankAccount("User1", 100);
    repo.save(a1);

    assertEquals(1, repo.findAll().size());

    repo.delete(a1.getId());

    assertTrue(repo.findAll().isEmpty());
  }
}
