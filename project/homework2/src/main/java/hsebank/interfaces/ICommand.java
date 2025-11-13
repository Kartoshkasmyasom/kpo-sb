package hsebank.interfaces;

/**
 * Generic command interface for executing operations.
 *
 * @param <R> the type of result returned by the command
 */
public interface ICommand<R> {

    /**
     * Executes the command and returns a result.
     *
     * @return the result of the command execution
     */
    R execute();
}