package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;

public class ListTrainersCommandTest {

    @Test
    public void execute_listIsFiltered_showsAllTrainers() {
        AddressBook ab = new AddressBook();
        Trainer trainerA = new Trainer(new Name("Alice"), new Phone("90000001"), new Email("a@example.com"),
                new HashSet<>());
        Trainer trainerB = new Trainer(new Name("Bob"), new Phone("90000002"), new Email("b@example.com"),
                new HashSet<>());
        ab.addPerson(trainerA);
        ab.addPerson(trainerB);

        Model model = new ModelManager(ab, new UserPrefs());
        model.updateFilteredTrainerList(new NameContainsKeywordsPredicate(List.of("Alice")));

        Model expectedModel = new ModelManager(ab, new UserPrefs());
        expectedModel.updateFilteredTrainerList(Model.PREDICATE_SHOW_ALL_PERSONS);

        ListTrainersCommand command = new ListTrainersCommand();
        assertCommandSuccess(command, model, ListTrainersCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(2, model.getFilteredTrainerList().size());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        ListTrainersCommand command = new ListTrainersCommand();
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }
}
