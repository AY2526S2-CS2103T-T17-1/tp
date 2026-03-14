package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRAINER;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddClientCommand;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class AddClientCommandParserTest {

    private final AddClientCommandParser parser = new AddClientCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        AddClientCommand expectedCommand = new AddClientCommand(new Name("Amy Bee"), new Phone("11111111"),
                Index.fromOneBased(1));
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + " " + PREFIX_TRAINER + "1", expectedCommand);
    }

    @Test
    public void parse_missingPrefix_failure() {
        assertParseFailure(parser, PHONE_DESC_AMY + " " + PREFIX_TRAINER + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddClientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_AMY + PHONE_DESC_AMY + " " + PREFIX_TRAINER + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddClientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTrainerIndex_failure() {
        assertParseFailure(parser, NAME_DESC_AMY + PHONE_DESC_AMY + " " + PREFIX_TRAINER + "0",
                seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX);
    }
}
