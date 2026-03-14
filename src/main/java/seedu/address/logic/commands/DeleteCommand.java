package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Client;
import seedu.address.model.person.Person;
import seedu.address.model.person.Trainer;

/**
 * Deletes a trainer or client identified using its displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a trainer or client using a list-scoped index.\n"
            + "Parameters: t/TRAINER_INDEX or c/CLIENT_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " t/1";

    public static final String MESSAGE_DELETE_TRAINER_SUCCESS = "Deleted Trainer: %1$s";
    public static final String MESSAGE_DELETE_CLIENT_SUCCESS = "Deleted Client: %1$s";
    public static final String MESSAGE_TRAINER_HAS_CLIENTS =
            "Cannot delete trainer: they still have active clients.";
    public static final String MESSAGE_NOT_A_TRAINER = "The person at the specified index is not a Trainer.";
    public static final String MESSAGE_NOT_A_CLIENT = "The person at the specified index is not a Client.";

    /** Target list that the index refers to. */
    public enum TargetType {
        TRAINER,
        CLIENT
    }

    private final TargetType targetType;
    private final Index targetIndex;

    /**
     * Creates a {@code DeleteCommand}.
     *
     * @param targetType Target list to delete from.
     * @param targetIndex Index of the person in the corresponding list.
     */
    public DeleteCommand(TargetType targetType, Index targetIndex) {
        requireNonNull(targetType);
        this.targetIndex = targetIndex;
        this.targetType = targetType;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = targetType == TargetType.TRAINER
                ? model.getFilteredTrainerList()
                : model.getFilteredClientList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());

        if (targetType == TargetType.TRAINER) {
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

        if (!(personToDelete instanceof Client)) {
            throw new CommandException(MESSAGE_NOT_A_CLIENT);
        }

        model.deletePerson(personToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_CLIENT_SUCCESS, Messages.format(personToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetType == otherDeleteCommand.targetType
                && targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetType", targetType)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
