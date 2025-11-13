package hsebank.client.commands;

import hsebank.domains.BankAccount;
import hsebank.facades.BankAccountFacade;
import hsebank.interfaces.ICommand;

/**
 * Command for creating a new bank account.
 */
public class CreateAccountCommand implements ICommand<BankAccount> {
    private final BankAccountFacade facade;
    private final String name;
    private final int balance;

    /**
     * Constructs a CreateAccountCommand with the specified parameters.
     *
     * @param facade the bank account facade to use for account creation
     * @param name the name of the new account
     * @param balance the initial balance of the new account
     */
    public CreateAccountCommand(final BankAccountFacade facade,
                                final String name,
                                final int balance) {
        this.facade = facade;
        this.name = name;
        this.balance = balance;
    }

    /**
     * Executes the account creation command.
     *
     * @return the created BankAccount object
     */
    @Override
    public BankAccount execute() {
        BankAccount acc = facade.create(name, balance);
        return acc;
    }
}