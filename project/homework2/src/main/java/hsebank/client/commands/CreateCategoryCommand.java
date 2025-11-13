package hsebank.client.commands;

import hsebank.domains.Category;
import hsebank.enums.CategoryType;
import hsebank.facades.CategoryFacade;
import hsebank.interfaces.ICommand;

/**
 * Command for creating a new category.
 */
public class CreateCategoryCommand implements ICommand<Category> {
    private final CategoryFacade facade;
    private final CategoryType type;
    private final String name;

    /**
     * Constructs a CreateCategoryCommand with the specified parameters.
     *
     * @param facade the category facade to use for category creation
     * @param type the type of the new category
     * @param name the name of the new category
     */
    public CreateCategoryCommand(final CategoryFacade facade,
                                 final CategoryType type,
                                 final String name) {
        this.facade = facade;
        this.type = type;
        this.name = name;
    }

    /**
     * Executes the category creation command.
     *
     * @return the created Category object
     */
    @Override
    public Category execute() {
        Category cat = facade.create(type, name);
        return cat;
    }
}