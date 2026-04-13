package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ListClientsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListClientsCommand object.
 */
public class ListClientsCommandParser implements Parser<ListClientsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListClientsCommand
     * and returns a ListClientsCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListClientsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            return new ListClientsCommand();
        }

        try {
            Index index = ParserUtil.parseIndex(trimmedArgs);
            return new ListClientsCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListClientsCommand.MESSAGE_USAGE), pe);
        }
    }
}
