package hsebank.client.commands;

import hsebank.facades.CategoryFacade;
import hsebank.interfaces.ICommand;

/**
 * Command for deleting a category.
 */
public class DeleteCategoryCommand implements ICommand<Void> {
    private final CategoryFacade facade;
    private final int id;

    /**
     * Constructs a DeleteCategoryCommand with the specified parameters.
     *
     * @param facade the category facade to use for deletion
     * @param id the ID of the category to delete
     */
    public DeleteCategoryCommand(final CategoryFacade facade, final int id) {
        this.facade = facade;
        this.id = id;
    }

    /**
     * Executes the category deletion command.
     *
     * @return null since this command doesn't return a value
     */
    @Override
    public Void execute() {
        facade.delete(id);
        return null;
    }
}