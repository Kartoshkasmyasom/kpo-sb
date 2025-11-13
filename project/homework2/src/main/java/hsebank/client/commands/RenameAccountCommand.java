package hsebank.client.commands;

import hsebank.facades.BankAccountFacade;
import hsebank.interfaces.ICommand;

/**
 * Command for renaming a bank account.
 */
public class RenameAccountCommand implements ICommand<Void> {
    private final BankAccountFacade facade;
    private final int id;
    private final String newName;

    /**
     * Constructs a RenameAccountCommand with the specified parameters.
     *
     * @param facade the bank account facade to use for renaming
     * @param id the ID of the account to rename
     * @param newName the new name for the account
     */
    public RenameAccountCommand(final BankAccountFacade facade,
                                final int id,
                                final String newName) {
        this.facade = facade;
        this.id = id;
        this.newName = newName;
    }

    /**
     * Executes the account renaming command.
     *
     * @return null since this command doesn't return a value
     */
    @Override
    public Void execute() {
        facade.rename(id, newName);
        return null;
    }
}