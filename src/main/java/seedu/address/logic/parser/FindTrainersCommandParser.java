package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindTrainersCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new {@code FindTrainersCommand} object.
 */
public class FindTrainersCommandParser implements Parser<FindTrainersCommand> {

    @Override
    public FindTrainersCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTrainersCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        for (String keyword : nameKeywords) {
            if (!keyword.matches("\\p{Alnum}+")) {
                throw new ParseException("Keywords must be alphanumeric.");
            }
        }

        return new FindTrainersCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }
}
