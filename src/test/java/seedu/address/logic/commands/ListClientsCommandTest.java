package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ListClientsCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;

public class ListClientsCommandTest {

    private AddressBook ab;
    private Trainer trainer;
    private Client clientA;
    private Client clientB;

    @BeforeEach
    public void setUp() {
        ab = new AddressBook();
        trainer = new Trainer(new Name("Trainer"), new Phone("90000000"), new Email("t@example.com"),
                new HashSet<>());
        clientA = new Client(new Name("Alice"), new Phone("80000001"), trainer.getPhone(), trainer.getName(),
                new HashSet<>());
        clientB = new Client(new Name("Bob"), new Phone("80000002"), trainer.getPhone(), trainer.getName(),
                new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(clientA);
        ab.addPerson(clientB);
    }

    // ──── No-index form ───────────────────────────────────────────────────────

    @Test
    public void execute_noIndex_showsAllClients() throws CommandException {
        Model model = new ModelManager(ab, new UserPrefs());
        model.updateFilteredClientList(new NameContainsKeywordsPredicate(List.of("Alice")));

        CommandResult result = new ListClientsCommand().execute(model);

        assertEquals(ListClientsCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertEquals(2, model.getFilteredClientList().size());
    }

    @Test
    public void execute_noIndex_clearTrainerFlagIsTrue() throws CommandException {
        Model model = new ModelManager(ab, new UserPrefs());
        CommandResult result = new ListClientsCommand().execute(model);
        assertTrue(result.isClearTrainer());
    }

    @Test
    public void execute_noIndex_clearsSelectedTrainer() throws CommandException {
        Model model = new ModelManager(ab, new UserPrefs());
        model.setSelectedTrainer(trainer);

        new ListClientsCommand().execute(model);

        assertTrue(model.getSelectedTrainer().isEmpty());
    }

    // ──── With-index form ─────────────────────────────────────────────────────

    @Test
    public void execute_withIndex_showsOnlyTrainersClients() throws CommandException {
        Model model = new ModelManager(ab, new UserPrefs());

        CommandResult result = new ListClientsCommand(Index.fromOneBased(1)).execute(model);

        assertEquals(String.format(ListClientsCommand.MESSAGE_SUCCESS_TRAINER, trainer.getName()),
                result.getFeedbackToUser());
        assertEquals(2, model.getFilteredClientList().size());
        assertFalse(model.getSelectedTrainer().isEmpty());
        assertEquals(trainer, model.getSelectedTrainer().get());
    }

    @Test
    public void execute_withIndex_clearTrainerFlagIsFalse() throws CommandException {
        Model model = new ModelManager(ab, new UserPrefs());
        CommandResult result = new ListClientsCommand(Index.fromOneBased(1)).execute(model);
        assertFalse(result.isClearTrainer());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Model model = new ModelManager(ab, new UserPrefs());
        ListClientsCommand command = new ListClientsCommand(Index.fromOneBased(99));
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    // ──── Null model ──────────────────────────────────────────────────────────

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ListClientsCommand().execute(null));
    }

    // ──── Parser ─────────────────────────────────────────────────────────────

    @Test
    public void parser_emptyArgs_returnsNoIndexForm() throws ParseException {
        ListClientsCommand cmd = new ListClientsCommandParser().parse("");
        assertEquals(new ListClientsCommand(), cmd);
    }

    @Test
    public void parser_validIndex_returnsIndexForm() throws ParseException {
        ListClientsCommand cmd = new ListClientsCommandParser().parse("1");
        assertEquals(new ListClientsCommand(Index.fromOneBased(1)), cmd);
    }

    @Test
    public void parser_invalidIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> new ListClientsCommandParser().parse("abc"));
    }

    @Test
    public void parser_zeroIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> new ListClientsCommandParser().parse("0"));
    }

    // ──── equals ─────────────────────────────────────────────────────────────

    @Test
    public void equals_sameNoIndex() {
        assertEquals(new ListClientsCommand(), new ListClientsCommand());
    }

    @Test
    public void equals_sameIndex() {
        assertEquals(new ListClientsCommand(Index.fromOneBased(1)),
                new ListClientsCommand(Index.fromOneBased(1)));
    }

    @Test
    public void equals_differentForms() {
        assertFalse(new ListClientsCommand().equals(new ListClientsCommand(Index.fromOneBased(1))));
    }
}
