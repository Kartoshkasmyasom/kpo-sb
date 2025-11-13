package hsebank.client.commands;

import hsebank.domains.BankAccount;
import hsebank.facades.BankAccountFacade;
import hsebank.interfaces.ICommand;
import java.util.List;

/**
 * Command for listing all bank accounts.
 */
public class ListAccountsCommand implements ICommand<List<BankAccount>> {
    private final BankAccountFacade facade;

    /**
     * Constructs a ListAccountsCommand with the specified facade.
     *
     * @param facade the bank account facade to use for listing accounts
     */
    public ListAccountsCommand(final BankAccountFacade facade) {
        this.facade = facade;
    }

    /**
     * Executes the account listing command.
     *
     * @return a list of all bank accounts
     */
    @Override
    public List<BankAccount> execute() {
        return facade.list_all_accounts();
    }
}