package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class FindTrainersCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
            new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
            new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindTrainersCommand findFirstCommand = new FindTrainersCommand(firstPredicate);
        FindTrainersCommand findSecondCommand = new FindTrainersCommand(secondPredicate);

        assertTrue(findFirstCommand.equals(findFirstCommand));
        assertTrue(findFirstCommand.equals(new FindTrainersCommand(firstPredicate)));
        assertFalse(findFirstCommand.equals(1));
        assertFalse(findFirstCommand.equals(null));
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_multipleKeywords_multipleTrainersFound() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Benson"));
        FindTrainersCommand command = new FindTrainersCommand(predicate);

        expectedModel.updateFilteredTrainerList(predicate);
        String expectedMessage = String.format(FindTrainersCommand.MESSAGE_TRAINERS_LISTED_OVERVIEW,
                expectedModel.getFilteredTrainerList().size());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getFilteredTrainerList(), model.getFilteredTrainerList());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("Alice"));
        FindTrainersCommand command = new FindTrainersCommand(predicate);
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void toStringMethod() {
        NameContainsKeywordsPredicate predicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("keyword"));
        FindTrainersCommand command = new FindTrainersCommand(predicate);
        String expected = FindTrainersCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }
}
