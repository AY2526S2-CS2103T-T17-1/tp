package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddTrainerCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class AddTrainerCommandParserTest {

    private final AddTrainerCommandParser parser = new AddTrainerCommandParser();

    @Test
    public void parse_missingPrefixes_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTrainerCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(""));
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        String userInput = "preamble n/Alice p/91234567 e/a@b.com";
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTrainerCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(userInput));
    }
}
