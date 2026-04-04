package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CALORIE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FOCUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRAINER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VALIDITY;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditClientCommand;
import seedu.address.logic.commands.EditClientCommand.EditClientDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditClientCommand object.
 */
public class EditClientCommandParser implements Parser<EditClientCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * EditClientCommand and returns an EditClientCommand object for
     * execution.
     *
     * @throws ParseException if the user input does not conform to the
     *     expected format.
     */
    public EditClientCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_NAME, PREFIX_PHONE, PREFIX_TRAINER, PREFIX_CALORIE,
                PREFIX_FOCUS, PREFIX_REMARK, PREFIX_VALIDITY);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    EditClientCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME, PREFIX_PHONE, PREFIX_TRAINER,
                PREFIX_CALORIE, PREFIX_FOCUS, PREFIX_REMARK,
                PREFIX_VALIDITY);

        EditClientDescriptor descriptor = new EditClientDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            descriptor.setName(ParserUtil.parseName(
                    argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            descriptor.setPhone(ParserUtil.parsePhone(
                    argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_TRAINER).isPresent()) {
            descriptor.setTrainerIndex(ParserUtil.parseIndex(
                    argMultimap.getValue(PREFIX_TRAINER).get()));
        }
        if (argMultimap.getValue(PREFIX_CALORIE).isPresent()) {
            descriptor.setCalorieTarget(ParserUtil.parseCalories(
                    argMultimap.getValue(PREFIX_CALORIE).get()));
        }
        if (argMultimap.getValue(PREFIX_FOCUS).isPresent()) {
            String focusValue = argMultimap.getValue(PREFIX_FOCUS).get().trim();
            if (focusValue.isEmpty()) {
                descriptor.setClearWorkoutFocus(true);
            } else {
                descriptor.setWorkoutFocus(ParserUtil.parseWorkoutFocus(focusValue));
            }
        }
        if (argMultimap.getValue(PREFIX_REMARK).isPresent()) {
            String remarkValue = argMultimap.getValue(PREFIX_REMARK).get().trim();
            if (remarkValue.isEmpty()) {
                descriptor.setClearRemark(true);
            } else {
                descriptor.setRemark(ParserUtil.parseRemark(remarkValue));
            }
        }
        if (argMultimap.getValue(PREFIX_VALIDITY).isPresent()) {
            String validityValue = argMultimap.getValue(PREFIX_VALIDITY).get().trim();
            if (validityValue.isEmpty()) {
                descriptor.setClearValidity(true);
            } else {
                descriptor.setValidity(ParserUtil.parseValidity(validityValue));
            }
        }

        if (!descriptor.isAnyFieldEdited()) {
            throw new ParseException(EditClientCommand.MESSAGE_NOT_EDITED);
        }

        return new EditClientCommand(index, descriptor);
    }
}
