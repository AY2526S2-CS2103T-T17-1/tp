package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Trainer;

/**
 * Adds a trainer to the address book.
 */
public class AddTrainerCommand extends Command {

    public static final String COMMAND_WORD = "add-t";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a trainer to the gym's roster. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE_NUMBER "
            + PREFIX_EMAIL + "EMAIL\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_SUCCESS = "New trainer added: %1$s. Phone: %2$s, Email: %3$s";
    public static final String MESSAGE_DUPLICATE_TRAINER = "This number is already in use, "
            + "or this email is already used by another person.";

    private final Trainer toAdd;

    /**
     * Creates an AddTrainerCommand to add the specified {@code Trainer}
     */
    public AddTrainerCommand(Trainer trainer) {
        requireNonNull(trainer);
        toAdd = trainer;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_TRAINER);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.getName(), toAdd.getPhone(), toAdd.getEmail()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddTrainerCommand // instanceof handles nulls
                && toAdd.equals(((AddTrainerCommand) other).toAdd));
    }
}
