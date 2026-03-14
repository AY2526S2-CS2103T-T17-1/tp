package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLIENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRAINER;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args == null ? "" : args.trim();
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + trimmedArgs, PREFIX_TRAINER, PREFIX_CLIENT);

        boolean hasTrainer = argMultimap.getValue(PREFIX_TRAINER).isPresent();
        boolean hasClient = argMultimap.getValue(PREFIX_CLIENT).isPresent();

        if (!argMultimap.getPreamble().isEmpty() || hasTrainer == hasClient) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getAllValues(PREFIX_TRAINER).size() > 1 || argMultimap.getAllValues(PREFIX_CLIENT).size() > 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        try {
            if (hasTrainer) {
                Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_TRAINER).get());
                return new DeleteCommand(DeleteCommand.TargetType.TRAINER, index);
            }

            Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CLIENT).get());
            return new DeleteCommand(DeleteCommand.TargetType.CLIENT, index);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }
    }

}
