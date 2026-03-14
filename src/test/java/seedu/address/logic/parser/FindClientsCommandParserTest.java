package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindClientsCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class FindClientsCommandParserTest {

    private final FindClientsCommandParser parser = new FindClientsCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindClientsCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindClientsCommand() {
        FindClientsCommand expectedCommand =
                new FindClientsCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));

        assertParseSuccess(parser, "Alice Bob", expectedCommand);
        assertParseSuccess(parser, " \n Alice \n\t Bob  \t", expectedCommand);
    }

    @Test
    public void parse_invalidKeyword_throwsParseException() {
        assertParseFailure(parser, "Alice B@b", "Keywords must be alphanumeric.");
    }
}
