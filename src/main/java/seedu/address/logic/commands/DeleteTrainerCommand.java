package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Trainer;

/**
 * Deletes a trainer identified using its displayed index from the address book.
 * Deletion is blocked if the trainer still has active clients.
 */
public class DeleteTrainerCommand extends Command {

    public static final String COMMAND_WORD = "delete-trainer";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the trainer identified by the index number used in the displayed trainer list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TRAINER_SUCCESS = "Deleted Trainer: %1$s";
    public static final String MESSAGE_NOT_A_TRAINER = "The person at the specified index is not a Trainer.";
    public static final String MESSAGE_TRAINER_HAS_CLIENTS =
            "Cannot delete trainer: they still have active clients.";

    private final Index targetIndex;

    public DeleteTrainerCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredTrainerList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());

        if (!(personToDelete instanceof Trainer)) {
            throw new CommandException(MESSAGE_NOT_A_TRAINER);
        }

        Trainer trainerToDelete = (Trainer) personToDelete;

        if (model.hasClientWithTrainer(trainerToDelete)) {
            throw new CommandException(MESSAGE_TRAINER_HAS_CLIENTS);
        }

        model.deletePerson(trainerToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_TRAINER_SUCCESS, Messages.format(trainerToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteTrainerCommand)) {
            return false;
        }

        DeleteTrainerCommand otherDeleteTrainerCommand = (DeleteTrainerCommand) other;
        return targetIndex.equals(otherDeleteTrainerCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
