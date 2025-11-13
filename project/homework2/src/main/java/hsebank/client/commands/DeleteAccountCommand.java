package hsebank.client.commands;

import hsebank.facades.BankAccountFacade;
import hsebank.interfaces.ICommand;

/**
 * Command for deleting a bank account.
 */
public class DeleteAccountCommand implements ICommand<Void> {
    private final BankAccountFacade facade;
    private final int id;

    /**
     * Constructs a DeleteAccountCommand with the specified parameters.
     *
     * @param facade the bank account facade to use for account deletion
     * @param id the ID of the account to delete
     */
    public DeleteAccountCommand(final BankAccountFacade facade, final int id) {
        this.facade = facade;
        this.id = id;
    }

    /**
     * Executes the account deletion command.
     *
     * @return null since this command doesn't return a value
     */
    @Override
    public Void execute() {
        facade.delete(id);
        return null;
    }
}