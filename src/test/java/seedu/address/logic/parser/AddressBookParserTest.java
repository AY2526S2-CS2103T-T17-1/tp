package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddClientCommand;
import seedu.address.logic.commands.AddTrainerCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteClientCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteTrainerCommand;
import seedu.address.logic.commands.EditClientCommand;
import seedu.address.logic.commands.EditClientCommand.EditClientDescriptor;
import seedu.address.logic.commands.EditTrainerCommand;
import seedu.address.logic.commands.EditTrainerCommand.EditTrainerDescriptor;
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
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.commands.SetCalorieTargetCommand;
import seedu.address.logic.commands.SetFocusCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Trainer;
import seedu.address.model.person.WorkoutFocus;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Trainer trainer = (Trainer) new PersonBuilder().build();
        AddTrainerCommand command = (AddTrainerCommand) parser.parseCommand(PersonUtil.getAddTrainerCommand(trainer));
        assertEquals(new AddTrainerCommand(trainer), command);
    }

    @Test
    public void parseCommand_addClient() throws Exception {
        AddClientCommand command = (AddClientCommand) parser.parseCommand(
                AddClientCommand.COMMAND_WORD + " n/Alice p/81234567 t/1");
        assertEquals(new AddClientCommand(new seedu.address.model.person.Name("Alice"),
                new seedu.address.model.person.Phone("81234567"), INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_editClient() throws Exception {
        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(new seedu.address.model.person.Name("Alice"));
        EditClientCommand command = (EditClientCommand) parser.parseCommand(
                EditClientCommand.COMMAND_WORD + " 1 n/Alice");
        assertEquals(new EditClientCommand(INDEX_FIRST_PERSON, descriptor),
                command);
    }

    @Test
    public void parseCommand_editTrainer() throws Exception {
        EditTrainerDescriptor descriptor = new EditTrainerDescriptor();
        descriptor.setName(new seedu.address.model.person.Name("John"));
        EditTrainerCommand command = (EditTrainerCommand) parser.parseCommand(
                EditTrainerCommand.COMMAND_WORD + " 1 n/John");
        assertEquals(new EditTrainerCommand(INDEX_FIRST_PERSON, descriptor),
                command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " t/" + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(DeleteCommand.TargetType.TRAINER, INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_deleteClient() throws Exception {
        DeleteClientCommand command = (DeleteClientCommand) parser.parseCommand(
                DeleteClientCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteClientCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_deleteTrainer() throws Exception {
        DeleteTrainerCommand command = (DeleteTrainerCommand) parser.parseCommand(
                DeleteTrainerCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteTrainerCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_setCalorieTarget() throws Exception {
        SetCalorieTargetCommand command = (SetCalorieTargetCommand) parser.parseCommand(
                SetCalorieTargetCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_PERSON.getOneBased() + " cal/2000");
        assertEquals(new SetCalorieTargetCommand(INDEX_FIRST_PERSON, 2000), command);
    }

    @Test
    public void parseCommand_logCalorieIntake() throws Exception {
        LogCalorieIntakeCommand command = (LogCalorieIntakeCommand) parser.parseCommand(
                LogCalorieIntakeCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_PERSON.getOneBased() + " cal/500");
        assertEquals(new LogCalorieIntakeCommand(INDEX_FIRST_PERSON, 500), command);
    }

    @Test
    public void parseCommand_setFocus() throws Exception {
        SetFocusCommand command = (SetFocusCommand) parser.parseCommand(
                SetFocusCommand.COMMAND_WORD + " c/" + INDEX_FIRST_PERSON.getOneBased() + " f/Chest");
        assertEquals(new SetFocusCommand(INDEX_FIRST_PERSON, new WorkoutFocus("Chest")), command);
    }

    @Test
    public void parseCommand_remark() throws Exception {
        RemarkCommand command = (RemarkCommand) parser.parseCommand(
                RemarkCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + " r/Needs follow-up");
        assertEquals(new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Needs follow-up")), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_export() throws Exception {
        ExportCommand command = (ExportCommand) parser.parseCommand(
                ExportCommand.COMMAND_WORD + " data/export.json");
        assertEquals(new ExportCommand(java.nio.file.Paths.get("data/export.json")), command);
    }

    @Test
    public void parseCommand_import() throws Exception {
        ImportCommand command = (ImportCommand) parser.parseCommand(
                ImportCommand.COMMAND_WORD + " data/import.json");
        assertEquals(new ImportCommand(java.nio.file.Paths.get("data/import.json")), command);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findTrainers() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindTrainersCommand command = (FindTrainersCommand) parser.parseCommand(
                FindTrainersCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindTrainersCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findClients() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindClientsCommand command = (FindClientsCommand) parser.parseCommand(
                FindClientsCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindClientsCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_listTrainers() throws Exception {
        assertTrue(parser.parseCommand(ListTrainersCommand.COMMAND_WORD) instanceof ListTrainersCommand);
    }

    @Test
    public void parseCommand_listClients() throws Exception {
        assertTrue(parser.parseCommand(ListClientsCommand.COMMAND_WORD) instanceof ListClientsCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                HelpCommand.MESSAGE_USAGE), () -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }

}
