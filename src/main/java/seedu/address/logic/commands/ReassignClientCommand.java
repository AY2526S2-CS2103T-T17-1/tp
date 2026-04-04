package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRAINER;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Client;
import seedu.address.model.person.Person;
import seedu.address.model.person.Trainer;

/**
 * Reassigns a client to a different trainer.
 */
public class ReassignClientCommand extends Command {

    public static final String COMMAND_WORD = "reassign-client";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Reassigns a client to a different trainer.\n"
            + "Parameters: CLIENT_INDEX "
            + PREFIX_TRAINER + "TRAINER_INDEX\n"
            + "Example: " + COMMAND_WORD + " 2 " + PREFIX_TRAINER + "1";

    public static final String MESSAGE_SUCCESS = "Reassigned %1$s to trainer %2$s.";
    public static final String MESSAGE_SAME_TRAINER = "Client is already assigned to this trainer.";
    public static final String MESSAGE_INVALID_CLIENT_INDEX = "The client index provided is invalid.";
    public static final String MESSAGE_INVALID_TRAINER_INDEX = "The trainer index provided is invalid.";

    private final Index clientIndex;
    private final Index trainerIndex;

    /**
     * Creates a {@code ReassignClientCommand}.
     *
     * @param clientIndex  The index of the client in the currently displayed client list.
     * @param trainerIndex The index of the new trainer in the currently displayed trainer list.
     */
    public ReassignClientCommand(Index clientIndex, Index trainerIndex) {
        requireNonNull(clientIndex);
        requireNonNull(trainerIndex);
        this.clientIndex = clientIndex;
        this.trainerIndex = trainerIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> clientList = model.getFilteredClientList();

        if (clientIndex.getZeroBased() >= clientList.size()) {
            throw new CommandException(MESSAGE_INVALID_CLIENT_INDEX);
        }

        Person personAtClientIndex = clientList.get(clientIndex.getZeroBased());
        if (!(personAtClientIndex instanceof Client)) {
            throw new CommandException(MESSAGE_INVALID_CLIENT_INDEX);
        }

        List<Person> trainerList = model.getFilteredTrainerList();

        if (trainerIndex.getZeroBased() >= trainerList.size()) {
            throw new CommandException(MESSAGE_INVALID_TRAINER_INDEX);
        }

        Person personAtTrainerIndex = trainerList.get(trainerIndex.getZeroBased());
        if (!(personAtTrainerIndex instanceof Trainer)) {
            throw new CommandException(MESSAGE_INVALID_TRAINER_INDEX);
        }

        Client clientToReassign = (Client) personAtClientIndex;
        Trainer newTrainer = (Trainer) personAtTrainerIndex;

        if (clientToReassign.getTrainerPhone().equals(newTrainer.getPhone())) {
            throw new CommandException(MESSAGE_SAME_TRAINER);
        }

        Client updatedClient = clientToReassign.withTrainer(newTrainer.getPhone(), newTrainer.getName());

        model.setPerson(clientToReassign, updatedClient);
        return new CommandResult(
                String.format(MESSAGE_SUCCESS, updatedClient.getName(), newTrainer.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ReassignClientCommand)) {
            return false;
        }

        ReassignClientCommand otherCommand = (ReassignClientCommand) other;
        return clientIndex.equals(otherCommand.clientIndex)
                && trainerIndex.equals(otherCommand.trainerIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientIndex, trainerIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("clientIndex", clientIndex)
                .add("trainerIndex", trainerIndex)
                .toString();
    }
}
