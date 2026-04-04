package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditClientCommand;
import seedu.address.logic.commands.EditClientCommand.EditClientDescriptor;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Validity;
import seedu.address.model.person.WorkoutFocus;

public class EditClientCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditClientCommand.MESSAGE_USAGE);

    private EditClientCommandParser parser = new EditClientCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, "n/Alice", MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1",
                EditClientCommand.MESSAGE_NOT_EDITED);

        // no index and no field
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5 n/Alice",
                MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0 n/Alice",
                MESSAGE_INVALID_FORMAT);

        // non-numeric
        assertParseFailure(parser, "abc n/Alice",
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
    }

    @Test
    public void parse_nameFieldOnly_success() {
        Index targetIndex = Index.fromOneBased(1);
        String userInput = "1 n/Alice";

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(new Name("Alice"));

        EditClientCommand expectedCommand =
                new EditClientCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_phoneFieldOnly_success() {
        Index targetIndex = Index.fromOneBased(2);
        String userInput = "2 p/99999999";

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setPhone(new Phone("99999999"));

        EditClientCommand expectedCommand =
                new EditClientCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_trainerIndexOnly_success() {
        Index targetIndex = Index.fromOneBased(1);
        String userInput = "1 t/2";

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setTrainerIndex(Index.fromOneBased(2));

        EditClientCommand expectedCommand =
                new EditClientCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_calorieTargetOnly_success() {
        Index targetIndex = Index.fromOneBased(1);
        String userInput = "1 cal/2000";

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setCalorieTarget(2000);

        EditClientCommand expectedCommand =
                new EditClientCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_workoutFocusOnly_success() {
        Index targetIndex = Index.fromOneBased(1);
        String userInput = "1 f/Chest";

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setWorkoutFocus(new WorkoutFocus("Chest"));

        EditClientCommand expectedCommand =
                new EditClientCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_remarkOnly_success() {
        Index targetIndex = Index.fromOneBased(1);
        String userInput = "1 r/Recovering";

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setRemark(new Remark("Recovering"));

        EditClientCommand expectedCommand =
                new EditClientCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validityOnly_success() {
        Index targetIndex = Index.fromOneBased(1);
        String userInput = "1 v/2027-12-31";

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setValidity(new Validity("2027-12-31"));

        EditClientCommand expectedCommand =
                new EditClientCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleFields_success() {
        Index targetIndex = Index.fromOneBased(1);
        String userInput = "1 n/Alice Tan p/88888888 cal/1500";

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(new Name("Alice Tan"));
        descriptor.setPhone(new Phone("88888888"));
        descriptor.setCalorieTarget(1500);

        EditClientCommand expectedCommand =
                new EditClientCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_allFields_success() {
        Index targetIndex = Index.fromOneBased(3);
        String userInput =
                "3 n/Alice p/99999999 t/1 cal/2000 f/Back r/Note v/2027-01-01";

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(new Name("Alice"));
        descriptor.setPhone(new Phone("99999999"));
        descriptor.setTrainerIndex(Index.fromOneBased(1));
        descriptor.setCalorieTarget(2000);
        descriptor.setWorkoutFocus(new WorkoutFocus("Back"));
        descriptor.setRemark(new Remark("Note"));
        descriptor.setValidity(new Validity("2027-01-01"));

        EditClientCommand expectedCommand =
                new EditClientCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
