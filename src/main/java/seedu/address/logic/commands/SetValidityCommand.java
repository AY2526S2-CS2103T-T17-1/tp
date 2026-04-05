package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VALIDITY;

import java.time.LocalDate;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Client;
import seedu.address.model.person.Person;
import seedu.address.model.person.Validity;

/**
 * Changes the membership validity of an existing client in the address book.
 */
public class SetValidityCommand extends Command {

    public static final String COMMAND_WORD = "set-validity";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the membership validity date of the client identified "
            + "by the index number used in the displayed person list. "
            + "Existing validity will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_VALIDITY + "VALIDITY\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_VALIDITY + "2026-12-31";

    public static final String MESSAGE_SET_VALIDITY_SUCCESS = "Set Validity to: %1$s for Client: %2$s";
    public static final String MESSAGE_SET_VALIDITY_PAST_DATE_WARNING =
            " (Warning: this date is in the past — membership is already expired!)";
    public static final String MESSAGE_INVALID_CLIENT = "The provided index does not correspond to a Client.";

    private final Index index;
    private final Validity validity;

    /**
     * @param index of the person in the filtered person list to edit the validity
     * @param validity of the person to be updated to
     */
    public SetValidityCommand(Index index, Validity validity) {
        requireNonNull(index);
        requireNonNull(validity);

        this.index = index;
        this.validity = validity;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredClientList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personAtIndex = lastShownList.get(index.getZeroBased());

        if (!(personAtIndex instanceof Client)) {
            throw new CommandException(MESSAGE_INVALID_CLIENT);
        }

        Client clientToEdit = (Client) personAtIndex;
        Client updatedClient = clientToEdit.withValidity(validity);

        model.setPerson(clientToEdit, updatedClient);
        String successMessage = String.format(MESSAGE_SET_VALIDITY_SUCCESS, validity, clientToEdit.getName());
        LocalDate parsedDate = LocalDate.parse(validity.value);
        if (parsedDate.isBefore(LocalDate.now())) {
            successMessage += MESSAGE_SET_VALIDITY_PAST_DATE_WARNING;
        }
        return new CommandResult(successMessage);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SetValidityCommand // instanceof handles nulls
                && index.equals(((SetValidityCommand) other).index)
                && validity.equals(((SetValidityCommand) other).validity));
    }
}
