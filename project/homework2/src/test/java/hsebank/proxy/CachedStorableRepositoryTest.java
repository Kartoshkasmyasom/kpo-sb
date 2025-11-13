package hsebank.proxy;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hsebank.domains.BankAccount;
import hsebank.repositories.StorableRepository;
import java.util.*;
import org.junit.jupiter.api.Test;

class CachedStorableRepositoryTest {

  @Test
  void findAll_usesUnderlyingRepositoryOnce() {
    StorableRepository<BankAccount> realRepo = mock(StorableRepository.class);

    realRepo.clear();
    List<BankAccount> list = List.of(new BankAccount("A", 10), new BankAccount("B", 20));
    when(realRepo.findAll()).thenReturn(list);

    CachedStorableRepository<BankAccount> cached = new CachedStorableRepository<>(realRepo);

    List<BankAccount> first = cached.findAll();
    List<BankAccount> second = cached.findAll();

    assertEquals(2, first.size());
    assertThat(first).containsExactlyElementsOf(second);

    verify(realRepo, times(1)).findAll();
  }
}
