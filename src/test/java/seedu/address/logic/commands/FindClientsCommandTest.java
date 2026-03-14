package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

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

public class FindClientsCommandTest {

    private static Model createModelWithClients() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("Trainer"), new Phone("90000000"), new Email("t@example.com"),
                new HashSet<>());
        Client clientA = new Client(new Name("Alice"), new Phone("80000001"), trainer.getPhone(), trainer.getName(),
                new HashSet<>());
        Client clientB = new Client(new Name("Benson"), new Phone("80000002"), trainer.getPhone(), trainer.getName(),
                new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(clientA);
        ab.addPerson(clientB);
        return new ModelManager(ab, new UserPrefs());
    }

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
            new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
            new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindClientsCommand findFirstCommand = new FindClientsCommand(firstPredicate);
        FindClientsCommand findSecondCommand = new FindClientsCommand(secondPredicate);

        assertTrue(findFirstCommand.equals(findFirstCommand));
        assertTrue(findFirstCommand.equals(new FindClientsCommand(firstPredicate)));
        assertFalse(findFirstCommand.equals(1));
        assertFalse(findFirstCommand.equals(null));
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_multipleKeywords_multipleClientsFound() {
        Model model = createModelWithClients();
        Model expectedModel = createModelWithClients();

        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Benson"));
        FindClientsCommand command = new FindClientsCommand(predicate);

        expectedModel.updateFilteredClientList(predicate);
        String expectedMessage = String.format(FindClientsCommand.MESSAGE_CLIENTS_LISTED_OVERVIEW,
                expectedModel.getFilteredClientList().size());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getFilteredClientList(), model.getFilteredClientList());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("Alice"));
        FindClientsCommand command = new FindClientsCommand(predicate);
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void toStringMethod() {
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("keyword"));
        FindClientsCommand command = new FindClientsCommand(predicate);
        String expected = FindClientsCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }
}
