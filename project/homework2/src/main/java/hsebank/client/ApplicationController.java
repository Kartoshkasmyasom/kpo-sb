package hsebank.client;

import hsebank.client.commands.CreateAccountCommand;
import hsebank.client.commands.CreateCategoryCommand;
import hsebank.client.commands.DeleteAccountCommand;
import hsebank.client.commands.DeleteCategoryCommand;
import hsebank.client.commands.DepositCommand;
import hsebank.client.commands.ListAccountsCommand;
import hsebank.client.commands.ListCategoriesCommand;
import hsebank.client.commands.RenameAccountCommand;
import hsebank.client.commands.RenameCategoryCommand;
import hsebank.client.commands.WithdrawCommand;
import hsebank.domains.BankAccount;
import hsebank.domains.Category;
import hsebank.enums.CategoryType;
import hsebank.facades.BankAccountFacade;
import hsebank.facades.CategoryFacade;
import hsebank.facades.OperationFacade;
import hsebank.interfaces.ICommand;
import hsebank.interfaces.IStorable;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import org.springframework.stereotype.Component;

/**
 * Application controller for handling user interactions in the banking system.
 * Provides a command-line interface for account and category management operations.
 */
@Component
public class ApplicationController {
    private final BankAccountFacade accountFacade;
    private final CategoryFacade categoryFacade;
    private final OperationFacade operationFacade;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Constructs an ApplicationController with the required facades.
     *
     * @param accountFacade the bank account facade
     * @param categoryFacade the category facade
     * @param operationFacade the operation facade
     */
    public ApplicationController(
            final BankAccountFacade accountFacade,
            final CategoryFacade categoryFacade,
            final OperationFacade operationFacade) {
        this.accountFacade = accountFacade;
        this.categoryFacade = categoryFacade;
        this.operationFacade = operationFacade;
    }

    /**
     * Safely parses an integer from user input with retry on failure.
     *
     * @param promptMessage the message to display when prompting for input
     * @return the parsed integer value
     */
    private int safeParseInt(final String promptMessage) {
        while (true) {
            try {
                return Integer.parseInt(prompt(promptMessage));
            } catch (NumberFormatException e) {
                System.out.println("Error: enter correct number!");
            }
        }
    }

    /**
     * Safely parses a CategoryType from user input with retry on failure.
     *
     * @return the parsed CategoryType value
     */
    private CategoryType safeParseCategoryType() {
        while (true) {
            try {
                return CategoryType.valueOf(prompt("type(INCOME|EXPENSE)").toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Error: Category type can only be INCOME or EXPENSE!");
            }
        }
    }

    /**
     * Prints an IStorable object in a formatted way.
     *
     * @param object the object to print
     */
    private void printObject(final IStorable object) {
        System.out.print("id: " + object.getId());

        if (object instanceof BankAccount) {
            BankAccount account = (BankAccount) object;
            System.out.print(" name: " + account.getName());
            System.out.print(" balance: " + account.getBalance());
        } else if (object instanceof Category) {
            Category category = (Category) object;
            System.out.print(" name: " + category.getName());
            System.out.print(" type: " + category.getType());
        }
        System.out.println();
    }

    /**
     * Main application loop that handles user input and command execution.
     */
    public void run() {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
            System.setErr(new PrintStream(System.err, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
            return;
        }

        while (true) {
            System.out.println(
                    "1 acc:create  2 acc:rename  3 acc:delete"
                            + " 4 acc:list  5 cat:create  6 cat:rename 7 cat:delete 8 cat:list "
                            + "9 deposit 10 withdraw 0 exit");
            String s = scanner.nextLine().trim();
            if (s.equals("0")) {
                break;
            }
            try {
                ICommand<?> command = createCommand(s);
                if (command == null) {
                    continue;
                }
                executeCommand(command);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Creates a command based on user input.
     *
     * @param input the user input string
     * @return the corresponding command or null if invalid
     */
    private ICommand<?> createCommand(final String input) {
        return switch (input) {
            case "1" -> new CreateAccountCommand(
                    accountFacade, prompt("name"), safeParseInt("balance"));
            case "2" -> new RenameAccountCommand(
                    accountFacade, safeParseInt("accountId"), prompt("newName"));
            case "3" -> new DeleteAccountCommand(accountFacade, safeParseInt("accountId"));
            case "4" -> new ListAccountsCommand(accountFacade);
            case "5" -> new CreateCategoryCommand(
                    categoryFacade, safeParseCategoryType(), prompt("name"));
            case "6" -> new RenameCategoryCommand(
                    categoryFacade, safeParseInt("categoryId"), prompt("name"));
            case "7" -> new DeleteCategoryCommand(categoryFacade, safeParseInt("categoryId"));
            case "8" -> new ListCategoriesCommand(categoryFacade);
            case "9" -> new DepositCommand(
                    operationFacade,
                    safeParseInt("accountId"),
                    safeParseInt("categoryId"),
                    safeParseInt("amount"));
            case "10" -> new WithdrawCommand(
                    operationFacade,
                    safeParseInt("accountId"),
                    safeParseInt("categoryId"),
                    safeParseInt("amount"));
            default -> null;
        };
    }

    /**
     * Executes a command and handles its result.
     *
     * @param command the command to execute
     */
    private void executeCommand(final ICommand<?> command) {
        Object result = command.execute();
        if (result instanceof Iterable) {
            for (IStorable object : (Iterable<? extends IStorable>) result) {
                printObject(object);
            }
        } else if (result instanceof IStorable) {
            printObject((IStorable) result);
        }
        System.out.println("Operation done successfully!");
    }

    /**
     * Prompts the user for input with the given label.
     *
     * @param label the label to display in the prompt
     * @return the user's input string
     */
    private String prompt(final String label) {
        System.out.print(label + ": ");
        return scanner.nextLine().trim();
    }
}