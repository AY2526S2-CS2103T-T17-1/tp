package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRAINER;

import java.util.HashSet;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Client;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;

/**
 * Adds a client to the system and assigns them to a trainer.
 */
public class AddClientCommand extends Command {

    public static final String COMMAND_WORD = "add-client";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a new client and assigns them to a trainer.\n"
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_TRAINER + "TRAINER_INDEX\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Alice Lim "
            + PREFIX_PHONE + "81234567 "
            + PREFIX_TRAINER + "1";

    public static final String MESSAGE_SUCCESS = "New client added: %1$s. Assigned to Trainer: %2$s";
    public static final String MESSAGE_DUPLICATE_CLIENT = "Client with this phone number already exists.";
    public static final String MESSAGE_INVALID_TRAINER = "The provided index does not correspond to a Trainer.";

    private final Name name;
    private final Phone phone;
    private final Index trainerIndex;

    /**
     * Creates an AddClientCommand to add a client assigned to the specified trainer index.
     */
    public AddClientCommand(Name name, Phone phone, Index trainerIndex) {
        requireNonNull(name);
        requireNonNull(phone);
        requireNonNull(trainerIndex);
        this.name = name;
        this.phone = phone;
        this.trainerIndex = trainerIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredTrainerList();

        if (trainerIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person targetPerson = lastShownList.get(trainerIndex.getZeroBased());

        if (!(targetPerson instanceof Trainer)) {
            throw new CommandException(MESSAGE_INVALID_TRAINER);
        }

        Trainer assignedTrainer = (Trainer) targetPerson;

        Client toAdd = new Client(name, phone, assignedTrainer.getPhone(), assignedTrainer.getName(), new HashSet<>());

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_CLIENT);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.getName(), assignedTrainer.getName()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddClientCommand // instanceof handles nulls
                && name.equals(((AddClientCommand) other).name)
                && phone.equals(((AddClientCommand) other).phone)
                && trainerIndex.equals(((AddClientCommand) other).trainerIndex));
    }
}
