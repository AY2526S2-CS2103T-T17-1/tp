package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
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

    @Test
    public void parse_duplicateNamePrefix_throwsParseException() {
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(" n/Alice n/Bob p/91234567 e/a@b.com"));
    }

    @Test
    public void parse_duplicatePhonePrefix_throwsParseException() {
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(" n/Alice p/967 p/982 e/a@b.com"));
    }

    @Test
    public void parse_duplicateEmailPrefix_throwsParseException() {
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(" n/Alice p/67 e/a@b.com e/b@c.com"));
    }

    @Test
    public void parse_unknownPrefix_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTrainerCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse(" n/John p/911 e/j@ee.com x/extra"));
    }
}
