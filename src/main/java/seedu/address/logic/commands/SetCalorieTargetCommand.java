package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CALORIE;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Client;
import seedu.address.model.person.Person;

/**
 * Sets the daily calorie target for a client identified by the displayed index.
 * The calorie target is the number of calories the client aims to consume per day.
 */
public class SetCalorieTargetCommand extends Command {

    public static final String COMMAND_WORD = "set-calorie-target";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets the daily calorie target for the client identified by the index"
            + " number used in the displayed client list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_CALORIE + "CALORIES (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_CALORIE + "2000";

    public static final String MESSAGE_SET_CALORIE_TARGET_SUCCESS =
            "Set calorie target for %1$s: %2$d kcal/day";
    public static final String MESSAGE_NOT_A_CLIENT =
            "The person at the specified index is not a Client.";

    private final Index targetIndex;
    private final int calorieTarget;

    /**
     * Constructs a {@code SetCalorieTargetCommand}.
     *
     * @param targetIndex   The 1-based index of the client in the displayed list.
     * @param calorieTarget The daily calorie target to set (must be a positive integer).
     */
    public SetCalorieTargetCommand(Index targetIndex, int calorieTarget) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
        this.calorieTarget = calorieTarget;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredClientList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personAtIndex = lastShownList.get(targetIndex.getZeroBased());

        if (!(personAtIndex instanceof Client)) {
            throw new CommandException(MESSAGE_NOT_A_CLIENT);
        }

        Client clientToEdit = (Client) personAtIndex;
        Client updatedClient = new Client(
                clientToEdit.getName(),
                clientToEdit.getPhone(),
                clientToEdit.getTrainerPhone(),
                clientToEdit.getTrainerName(),
                clientToEdit.getTags(),
                calorieTarget,
                clientToEdit.getCalorieIntake(),
                clientToEdit.getWorkoutFocus(),
                clientToEdit.getRemark()
        );

        model.setPerson(clientToEdit, updatedClient);
        return new CommandResult(String.format(MESSAGE_SET_CALORIE_TARGET_SUCCESS,
                updatedClient.getName(), calorieTarget));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SetCalorieTargetCommand)) {
            return false;
        }

        SetCalorieTargetCommand otherCommand = (SetCalorieTargetCommand) other;
        return targetIndex.equals(otherCommand.targetIndex)
                && calorieTarget == otherCommand.calorieTarget;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("calorieTarget", calorieTarget)
                .toString();
    }
}
