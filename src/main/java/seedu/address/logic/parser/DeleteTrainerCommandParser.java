package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteTrainerCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteTrainerCommand object.
 */
public class DeleteTrainerCommandParser implements Parser<DeleteTrainerCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteTrainerCommand
     * and returns a DeleteTrainerCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteTrainerCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteTrainerCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTrainerCommand.MESSAGE_USAGE), pe);
        }
    }
}
