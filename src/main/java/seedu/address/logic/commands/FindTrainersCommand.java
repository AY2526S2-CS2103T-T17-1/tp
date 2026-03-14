package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Finds and lists all trainers whose names contain any of the argument keywords.
 */
public class FindTrainersCommand extends Command {

    public static final String COMMAND_WORD = "find-trainers";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all trainers whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them in the trainer list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob";

    public static final String MESSAGE_TRAINERS_LISTED_OVERVIEW = "%1$d trainers listed!";

    private final NameContainsKeywordsPredicate predicate;

    public FindTrainersCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredTrainerList(predicate);
        return new CommandResult(String.format(MESSAGE_TRAINERS_LISTED_OVERVIEW,
                model.getFilteredTrainerList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof FindTrainersCommand)) {
            return false;
        }

        FindTrainersCommand otherCommand = (FindTrainersCommand) other;
        return predicate.equals(otherCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
