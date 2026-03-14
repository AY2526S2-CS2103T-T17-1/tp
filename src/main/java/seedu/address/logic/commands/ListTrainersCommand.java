package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.model.Model;

/**
 * Lists all trainers in the address book to the user.
 */
public class ListTrainersCommand extends Command {

    public static final String COMMAND_WORD = "list-trainers";

    public static final String MESSAGE_SUCCESS = "Listed all trainers";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredTrainerList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
