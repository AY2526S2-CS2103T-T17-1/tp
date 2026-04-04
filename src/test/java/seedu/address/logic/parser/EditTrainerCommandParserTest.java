package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditTrainerCommand;
import seedu.address.logic.commands.EditTrainerCommand.EditTrainerDescriptor;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class EditTrainerCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditTrainerCommand.MESSAGE_USAGE);

    private EditTrainerCommandParser parser =
            new EditTrainerCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, "n/John", MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1",
                EditTrainerCommand.MESSAGE_NOT_EDITED);

        // no index and no field
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5 n/John",
                MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0 n/John",
                MESSAGE_INVALID_FORMAT);

        // non-numeric
        assertParseFailure(parser, "abc n/John",
                MESSAGE_INVALID_FORMAT);

        // preamble with extra text
        assertParseFailure(parser, "1 some random text",
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid phone
        assertParseFailure(parser, "1 p/",
                Phone.MESSAGE_CONSTRAINTS);

        // invalid name
        assertParseFailure(parser, "1 n/",
                Name.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, "1 e/notanemail",
                Email.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_nameFieldOnly_success() {
        Index targetIndex = Index.fromOneBased(1);
        String userInput = "1 n/John Tan";

        EditTrainerDescriptor descriptor = new EditTrainerDescriptor();
        descriptor.setName(new Name("John Tan"));

        EditTrainerCommand expectedCommand =
                new EditTrainerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_phoneFieldOnly_success() {
        Index targetIndex = Index.fromOneBased(2);
        String userInput = "2 p/99999999";

        EditTrainerDescriptor descriptor = new EditTrainerDescriptor();
        descriptor.setPhone(new Phone("99999999"));

        EditTrainerCommand expectedCommand =
                new EditTrainerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_emailFieldOnly_success() {
        Index targetIndex = Index.fromOneBased(1);
        String userInput = "1 e/newemail@example.com";

        EditTrainerDescriptor descriptor = new EditTrainerDescriptor();
        descriptor.setEmail(new Email("newemail@example.com"));

        EditTrainerCommand expectedCommand =
                new EditTrainerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_allFields_success() {
        Index targetIndex = Index.fromOneBased(1);
        String userInput =
                "1 n/Jane p/88888888 e/jane@example.com";

        EditTrainerDescriptor descriptor = new EditTrainerDescriptor();
        descriptor.setName(new Name("Jane"));
        descriptor.setPhone(new Phone("88888888"));
        descriptor.setEmail(new Email("jane@example.com"));

        EditTrainerCommand expectedCommand =
                new EditTrainerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleFields_success() {
        Index targetIndex = Index.fromOneBased(3);
        String userInput = "3 n/Mike e/mike@gym.com";

        EditTrainerDescriptor descriptor = new EditTrainerDescriptor();
        descriptor.setName(new Name("Mike"));
        descriptor.setEmail(new Email("mike@gym.com"));

        EditTrainerCommand expectedCommand =
                new EditTrainerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
