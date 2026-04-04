package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VALIDITY;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.SetValidityCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Validity;

/**
 * Parses input arguments and creates a new SetValidityCommand object
 */
public class SetValidityCommandParser implements Parser<SetValidityCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SetValidityCommand
     * and returns a SetValidityCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SetValidityCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_VALIDITY);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_VALIDITY);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, SetValidityCommand.MESSAGE_USAGE), ive);
        }

        if (argMultimap.getValue(PREFIX_VALIDITY).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetValidityCommand.MESSAGE_USAGE));
        }

        Validity validity = ParserUtil.parseValidity(argMultimap.getValue(PREFIX_VALIDITY).get());

        return new SetValidityCommand(index, validity);
    }
}
