package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Trainer;

/**
 * Lists clients in the address book to the user.
 * Without an index, all clients are shown and any trainer selection is cleared.
 * With an index, shows only clients belonging to the trainer at that index.
 */
public class ListClientsCommand extends Command {

    public static final String COMMAND_WORD = "list-clients";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists clients. Without an index, shows all clients and clears trainer selection.\n"
            + "With a trainer index, shows only clients for that trainer.\n"
            + "Parameters: [INDEX] (must be a positive integer if provided)\n"
            + "Example: " + COMMAND_WORD + "\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Listed all clients";
    public static final String MESSAGE_SUCCESS_TRAINER = "Listed clients for trainer: %1$s";
    public static final String MESSAGE_INVALID_TRAINER_INDEX = "The trainer index provided is invalid.";

    private final Optional<Index> trainerIndex;

    /**
     * Creates a ListClientsCommand that shows all clients and clears any trainer selection.
     */
    public ListClientsCommand() {
        this.trainerIndex = Optional.empty();
    }

    /**
     * Creates a ListClientsCommand that shows only clients for the trainer at {@code index}.
     */
    public ListClientsCommand(Index index) {
        requireNonNull(index);
        this.trainerIndex = Optional.of(index);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (trainerIndex.isEmpty()) {
            // No index: clear trainer selection and show all clients
            model.clearSelectedTrainer();
            model.updateFilteredClientList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(MESSAGE_SUCCESS, false, false, true);
        }

        // With index: select the trainer at the given index
        List<Person> trainerList = model.getFilteredTrainerList();
        int zeroBased = trainerIndex.get().getZeroBased();

        if (zeroBased >= trainerList.size()) {
            throw new CommandException(MESSAGE_INVALID_TRAINER_INDEX);
        }

        Person person = trainerList.get(zeroBased);
        if (!(person instanceof Trainer)) {
            throw new CommandException(MESSAGE_INVALID_TRAINER_INDEX);
        }

        Trainer trainer = (Trainer) person;
        model.setSelectedTrainer(trainer);
        return new CommandResult(String.format(MESSAGE_SUCCESS_TRAINER, trainer.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ListClientsCommand)) {
            return false;
        }
        ListClientsCommand otherCommand = (ListClientsCommand) other;
        return trainerIndex.equals(otherCommand.trainerIndex);
    }
}
