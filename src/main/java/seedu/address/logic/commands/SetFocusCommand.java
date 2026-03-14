package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLIENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FOCUS;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Client;
import seedu.address.model.person.Person;
import seedu.address.model.person.WorkoutFocus;

/**
 * Sets the current workout focus for a client.
 */
public class SetFocusCommand extends Command {

    public static final String COMMAND_WORD = "set-focus";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets the current primary workout focus for a client.\n"
            + "Parameters: "
            + PREFIX_CLIENT + "CLIENT_INDEX "
            + PREFIX_FOCUS + "FOCUS\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_CLIENT + "1 " + PREFIX_FOCUS + "Chest";

    public static final String MESSAGE_SUCCESS = "Workout focus for %1$s set to: %2$s.";
    public static final String MESSAGE_INVALID_CLIENT_INDEX = "The client index provided is invalid.";

    private final Index clientIndex;
    private final WorkoutFocus workoutFocus;

    /**
     * Creates a {@code SetFocusCommand}.
     *
     * @param clientIndex The index of the client in the currently displayed list.
     * @param workoutFocus The workout focus to set.
     */
    public SetFocusCommand(Index clientIndex, WorkoutFocus workoutFocus) {
        requireNonNull(clientIndex);
        requireNonNull(workoutFocus);
        this.clientIndex = clientIndex;
        this.workoutFocus = workoutFocus;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredClientList();

        if (clientIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_CLIENT_INDEX);
        }

        Person personAtIndex = lastShownList.get(clientIndex.getZeroBased());
        if (!(personAtIndex instanceof Client)) {
            throw new CommandException(MESSAGE_INVALID_CLIENT_INDEX);
        }

        Client clientToEdit = (Client) personAtIndex;
        Client updatedClient = new Client(
                clientToEdit.getName(),
                clientToEdit.getPhone(),
                clientToEdit.getTrainerPhone(),
                clientToEdit.getTrainerName(),
                clientToEdit.getTags(),
                clientToEdit.getCalorieTarget(),
                clientToEdit.getCalorieIntake(),
                java.util.Optional.of(workoutFocus),
                clientToEdit.getRemark()
        );

        model.setPerson(clientToEdit, updatedClient);
        return new CommandResult(String.format(MESSAGE_SUCCESS, updatedClient.getName(), workoutFocus.value));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SetFocusCommand)) {
            return false;
        }

        SetFocusCommand otherCommand = (SetFocusCommand) other;
        return clientIndex.equals(otherCommand.clientIndex)
                && workoutFocus.equals(otherCommand.workoutFocus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientIndex, workoutFocus);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("clientIndex", clientIndex)
                .add("workoutFocus", workoutFocus)
                .toString();
    }
}

