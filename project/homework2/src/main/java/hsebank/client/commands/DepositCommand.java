package hsebank.client.commands;

import hsebank.enums.OperationType;
import hsebank.facades.OperationFacade;
import hsebank.interfaces.ICommand;
import java.time.LocalDate;

/**
 * Command for depositing money into an account.
 */
public class DepositCommand implements ICommand<Void> {
    private final OperationFacade facade;
    private final int accountId;
    private final int categoryId;
    private final int amount;

    /**
     * Constructs a DepositCommand with the specified parameters.
     *
     * @param facade the operation facade to use for the deposit
     * @param accountId the ID of the account to deposit to
     * @param categoryId the ID of the category for the operation
     * @param amount the amount to deposit
     */
    public DepositCommand(final OperationFacade facade,
                          final int accountId,
                          final int categoryId,
                          final int amount) {
        this.facade = facade;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.amount = amount;
    }

    /**
     * Executes the deposit operation command.
     *
     * @return null since this command doesn't return a value
     */
    @Override
    public Void execute() {
        facade.create(OperationType.INCOME, accountId, categoryId, amount, LocalDate.now());
        return null;
    }
}