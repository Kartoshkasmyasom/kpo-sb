package hsebank.client.commands;

import hsebank.facades.CategoryFacade;
import hsebank.interfaces.ICommand;

/**
 * Command for renaming a category.
 */
public class RenameCategoryCommand implements ICommand<Void> {
    private final CategoryFacade facade;
    private final int id;
    private final String newName;

    /**
     * Constructs a RenameCategoryCommand with the specified parameters.
     *
     * @param facade the category facade to use for renaming
     * @param id the ID of the category to rename
     * @param newName the new name for the category
     */
    public RenameCategoryCommand(final CategoryFacade facade,
                                 final int id,
                                 final String newName) {
        this.facade = facade;
        this.id = id;
        this.newName = newName;
    }

    /**
     * Executes the category renaming command.
     *
     * @return null since this command doesn't return a value
     */
    @Override
    public Void execute() {
        facade.rename(id, newName);
        return null;
    }
}