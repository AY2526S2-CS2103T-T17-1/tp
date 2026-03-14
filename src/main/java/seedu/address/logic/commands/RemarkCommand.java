package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Client;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;

/**
 * Adds or overwrites a remark for a client identified by the displayed index.
 */
public class RemarkCommand extends Command {

    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a remark to an existing client. If a remark already exists, it will be overwritten.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_REMARK + "REMARK\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_REMARK + "Recovering from ACL surgery";

    public static final String MESSAGE_SUCCESS = "Added remark to Client: %1$s. Remark: %2$s";
    public static final String MESSAGE_INVALID_CLIENT_INDEX = "The client index provided is invalid.";

    private final Index clientIndex;
    private final Remark remark;

    /**
     * Creates a {@code RemarkCommand}.
     *
     * @param clientIndex The index of the client in the currently displayed list.
     * @param remark The remark to add.
     */
    public RemarkCommand(Index clientIndex, Remark remark) {
        requireNonNull(clientIndex);
        requireNonNull(remark);
        this.clientIndex = clientIndex;
        this.remark = remark;
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
                clientToEdit.getWorkoutFocus(),
                java.util.Optional.of(remark)
        );

        model.setPerson(clientToEdit, updatedClient);
        return new CommandResult(String.format(MESSAGE_SUCCESS, updatedClient.getName(), remark.value));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RemarkCommand)) {
            return false;
        }

        RemarkCommand otherCommand = (RemarkCommand) other;
        return clientIndex.equals(otherCommand.clientIndex)
                && remark.equals(otherCommand.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientIndex, remark);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("clientIndex", clientIndex)
                .add("remark", remark)
                .toString();
    }
}

