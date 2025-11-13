package hsebank.client.commands;

import hsebank.domains.Category;
import hsebank.facades.CategoryFacade;
import hsebank.interfaces.ICommand;
import java.util.List;

/**
 * Command for listing all categories.
 */
public class ListCategoriesCommand implements ICommand<List<Category>> {
    private final CategoryFacade facade;

    /**
     * Constructs a ListCategoriesCommand with the specified facade.
     *
     * @param facade the category facade to use for listing categories
     */
    public ListCategoriesCommand(final CategoryFacade facade) {
        this.facade = facade;
    }

    /**
     * Executes the category listing command.
     *
     * @return a list of all categories
     */
    @Override
    public List<Category> execute() {
        return facade.list_all_categories();
    }
}