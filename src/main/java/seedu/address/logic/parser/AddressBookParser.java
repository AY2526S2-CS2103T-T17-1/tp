package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddClientCommand;
import seedu.address.logic.commands.AddTrainerCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteClientCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteTrainerCommand;
import seedu.address.logic.commands.EditClientCommand;
import seedu.address.logic.commands.EditTrainerCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.commands.FindClientsCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindTrainersCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.commands.ListClientsCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListTrainersCommand;
import seedu.address.logic.commands.LogCalorieIntakeCommand;
import seedu.address.logic.commands.ReassignClientCommand;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.commands.SetCalorieTargetCommand;
import seedu.address.logic.commands.SetFocusCommand;
import seedu.address.logic.commands.SetValidityCommand;
import seedu.address.logic.commands.StatsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(AddressBookParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord").toLowerCase();
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level
        // (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case AddTrainerCommand.COMMAND_WORD:
            return new AddTrainerCommandParser().parse(arguments);

        case AddClientCommand.COMMAND_WORD:
            return new AddClientCommandParser().parse(arguments);

        case EditClientCommand.COMMAND_WORD:
            return new EditClientCommandParser().parse(arguments);
        case EditTrainerCommand.COMMAND_WORD:
            return new EditTrainerCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case DeleteClientCommand.COMMAND_WORD:
            return new DeleteClientCommandParser().parse(arguments);

        case DeleteTrainerCommand.COMMAND_WORD:
            return new DeleteTrainerCommandParser().parse(arguments);

        case SetCalorieTargetCommand.COMMAND_WORD:
            return new SetCalorieTargetCommandParser().parse(arguments);

        case LogCalorieIntakeCommand.COMMAND_WORD:
            return new LogCalorieIntakeCommandParser().parse(arguments);

        case SetFocusCommand.COMMAND_WORD:
            return new SetFocusCommandParser().parse(arguments);

        case SetValidityCommand.COMMAND_WORD:
            return new SetValidityCommandParser().parse(arguments);

        case ReassignClientCommand.COMMAND_WORD:
            return new ReassignClientCommandParser().parse(arguments);

        case RemarkCommand.COMMAND_WORD:
            return new RemarkCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case FindTrainersCommand.COMMAND_WORD:
            return new FindTrainersCommandParser().parse(arguments);

        case FindClientsCommand.COMMAND_WORD:
            return new FindClientsCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ListTrainersCommand.COMMAND_WORD:
            return new ListTrainersCommand();

        case StatsCommand.COMMAND_WORD:
            return new StatsCommand();

        case ListClientsCommand.COMMAND_WORD:
            return new ListClientsCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case ExportCommand.COMMAND_WORD:
            return new ExportCommandParser().parse(arguments);

        case ImportCommand.COMMAND_WORD:
            return new ImportCommandParser().parse(arguments);

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
