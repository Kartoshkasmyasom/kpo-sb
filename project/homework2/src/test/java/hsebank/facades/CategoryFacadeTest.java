package hsebank.facades;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hsebank.domains.Category;
import hsebank.enums.CategoryType;
import hsebank.factories.BankFactory;
import hsebank.interfaces.IRepository;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryFacadeTest {

  private IRepository<Category> repo;
  private BankFactory factory;
  private CategoryFacade facade;

  @BeforeEach
  void setUp() {
    repo = mock(IRepository.class);
    factory = new BankFactory();
    facade = new CategoryFacade(repo, factory);
  }

  @Test
  void create_validCategory_saves() {
    Category c = facade.create(CategoryType.INCOME, "Salary");
    assertEquals("Salary", c.getName());
    assertEquals(CategoryType.INCOME, c.getType());
    verify(repo).save(c);
  }

  @Test
  void create_invalidName_throws() {
    assertThrows(Exception.class, () -> facade.create(CategoryType.EXPENSE, "   "));
  }

  @Test
  void rename_valid_works() {
    Category c = new Category(CategoryType.EXPENSE, "Old");
    when(repo.findById(c.getId())).thenReturn(Optional.of(c));

    facade.rename(c.getId(), "New");

    assertEquals("New", c.getName());
    verify(repo).save(c);
  }

  @Test
  void delete_existing_callsRepo() {
    Category c = new Category(CategoryType.EXPENSE, "Food");
    facade.delete(c.getId());
    verify(repo).delete(c.getId());
  }
}
