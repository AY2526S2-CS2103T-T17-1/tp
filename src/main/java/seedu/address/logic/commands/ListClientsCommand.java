package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;
import seedu.address.model.person.Client;

/**
 * Lists all clients in the address book to the user.
 * Filters the person list to show only {@code Client} instances.
 */
public class ListClientsCommand extends Command {

    public static final String COMMAND_WORD = "list-clients";

    public static final String MESSAGE_SUCCESS = "Listed all clients";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all clients in the system.\n"
            + "Example: " + COMMAND_WORD;

    // TODO: Replace with a dedicated client list when Client model is fully
    // separated.
    public static final java.util.function.Predicate<seedu.address.model.person.Person> PREDICATE_SHOW_ALL_CLIENTS = person -> person instanceof Client;

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_CLIENTS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
