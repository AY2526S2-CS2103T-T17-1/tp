package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;
import seedu.address.model.person.Trainer;

/**
 * Lists all trainers in the address book to the user.
 * Filters the person list to show only {@code Trainer} instances.
 */
public class ListTrainersCommand extends Command {

    public static final String COMMAND_WORD = "list-trainers";

    public static final String MESSAGE_SUCCESS = "Listed all trainers";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all trainers in the system.\n"
            + "Example: " + COMMAND_WORD;

    // TODO: Replace with a dedicated trainer list when Trainer model is fully
    // separated.
    public static final java.util.function.Predicate<seedu.address.model.person.Person> PREDICATE_SHOW_ALL_TRAINERS = person -> person instanceof Trainer;

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_TRAINERS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
