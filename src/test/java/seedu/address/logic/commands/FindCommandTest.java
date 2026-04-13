package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

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

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        NameContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_singlePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        NameContainsKeywordsPredicate predicate = preparePredicate("Alice Pauline");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(seedu.address.testutil.TypicalPersons.ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_preservesSelectedTrainer() throws Exception {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        ab.addPerson(trainer);
        Model model = new ModelManager(ab, new UserPrefs());

        // Select the trainer before running find
        model.setSelectedTrainer(trainer);
        assertTrue(model.getSelectedTrainer().isPresent());

        // Run find with a keyword that matches nothing
        new FindCommand(preparePredicate("Nonexistent")).execute(model);

        // Trainer selection should be preserved across find
        assertTrue(model.getSelectedTrainer().isPresent());
        assertEquals(trainer, model.getSelectedTrainer().get());
    }

    @Test
    public void execute_selectedTrainerActive_messageReflectsActuallyDisplayedCards() {
        AddressBook ab = new AddressBook();

        Trainer bernice = new Trainer(new Name("Bernice Yu"), new Phone("90000000"),
                new Email("bernice@example.com"), new HashSet<>());
        Trainer alex = new Trainer(new Name("Alex Yeoh"), new Phone("91111111"),
                new Email("alex@example.com"), new HashSet<>());

        // David matches the find predicate, but is assigned to Alex.
        Client david = new Client(new Name("David Li"), new Phone("92222222"),
                alex.getPhone(), alex.getName(), new HashSet<>());

        ab.addPerson(bernice);
        ab.addPerson(alex);
        ab.addPerson(david);

        Model model = new ModelManager(ab, new UserPrefs());
        model.setSelectedTrainer(bernice);

        FindCommand command = new FindCommand(preparePredicate("David"));
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);

        CommandResult result = command.execute(model);
        assertEquals(expectedMessage, result.getFeedbackToUser());

        // Sanity: nothing should be displayed in either list.
        assertEquals(0, model.getFilteredTrainerList().size());
        assertEquals(0, model.getFilteredClientList().size());
    }

    @Test
    public void toStringMethod() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("keyword"));
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private NameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
