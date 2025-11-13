package hsebank.config;

import hsebank.domains.BankAccount;
import hsebank.domains.Category;
import hsebank.domains.Operation;
import hsebank.facades.BankAccountFacade;
import hsebank.facades.CategoryFacade;
import hsebank.facades.OperationFacade;
import hsebank.factories.BankFactory;
import hsebank.interfaces.IRepository;
import hsebank.proxy.CachedStorableRepository;
import hsebank.repositories.StorableRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for defining beans and dependency injection.
 */
@Configuration
public class AppConfig {

    /**
     * Creates a BankFactory bean.
     *
     * @return the BankFactory instance
     */
    @Bean
    BankFactory bankFactory() {
        return new BankFactory();
    }

    /**
     * Creates a cached repository for BankAccount entities.
     *
     * @return the BankAccount repository
     */
    @Bean
    IRepository<BankAccount> bankAccountRepository() {
        try {
            return new CachedStorableRepository<>(
                    new StorableRepository<>("bank_accounts.json", BankAccount.class));
        } catch (Exception e) {
            throw new org.springframework.beans.factory.BeanCreationException(
                    "bankAccountRepository", "Init failed", e);
        }
    }

    /**
     * Creates a cached repository for Category entities.
     *
     * @return the Category repository
     */
    @Bean
    IRepository<Category> categoryRepository() {
        try {
            return new CachedStorableRepository<Category>(
                    new StorableRepository<>("category.json", Category.class));
        } catch (Exception e) {
            throw new org.springframework.beans.factory.BeanCreationException(
                    "categoryRepository", "Init failed", e);
        }
    }

    /**
     * Creates a cached repository for Operation entities.
     *
     * @return the Operation repository
     */
    @Bean
    IRepository<Operation> operationRepository() {
        try {
            return new CachedStorableRepository<Operation>(
                    new StorableRepository<>("operation.json", Operation.class));
        } catch (Exception e) {
            throw new org.springframework.beans.factory.BeanCreationException(
                    "operationRepository", "Init failed", e);
        }
    }

    /**
     * Creates a BankAccountFacade bean.
     *
     * @param bankAccountRepository the bank account repository
     * @param factory the bank factory
     * @return the BankAccountFacade instance
     */
    @Bean
    BankAccountFacade bankAccountFacade(
            final IRepository<BankAccount> bankAccountRepository,
            final BankFactory factory) {
        return new BankAccountFacade(bankAccountRepository, factory);
    }

    /**
     * Creates a CategoryFacade bean.
     *
     * @param categoryRepository the category repository
     * @param factory the bank factory
     * @return the CategoryFacade instance
     */
    @Bean
    CategoryFacade categoryFacade(
            final IRepository<Category> categoryRepository,
            final BankFactory factory) {
        return new CategoryFacade(categoryRepository, factory);
    }

    /**
     * Creates an OperationFacade bean.
     *
     * @param operationRepository the operation repository
     * @param bankAccountFacade the bank account facade
     * @param categoryFacade the category facade
     * @param factory the bank factory
     * @return the OperationFacade instance
     */
    @Bean
    OperationFacade operationFacade(
            final IRepository<Operation> operationRepository,
            final BankAccountFacade bankAccountFacade,
            final CategoryFacade categoryFacade,
            final BankFactory factory) {
        return new OperationFacade(operationRepository, bankAccountFacade, categoryFacade, factory);
    }
}