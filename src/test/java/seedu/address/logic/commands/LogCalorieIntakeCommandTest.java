package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ModelStub;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code LogCalorieIntakeCommand}.
 */
public class LogCalorieIntakeCommandTest {

    @Test
    public void execute_validIndexClientInList_success() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(client);
        Model model = new ModelManager(ab, new UserPrefs());

        int loggedCalories = 500;
        // Client is at index 1 (1-based) in the client list
        LogCalorieIntakeCommand command = new LogCalorieIntakeCommand(INDEX_FIRST_PERSON, loggedCalories);

        Client updatedClient = new Client(client.getName(), client.getPhone(),
                client.getTrainerPhone(), client.getTrainerName(), client.getTags(),
                client.getCalorieTarget(), loggedCalories);
        String expectedMessage = String.format(LogCalorieIntakeCommand.MESSAGE_LOG_CALORIE_SUCCESS,
                updatedClient.getName(), loggedCalories, loggedCalories);

        AddressBook expectedAb = new AddressBook();
        expectedAb.addPerson(trainer);
        expectedAb.addPerson(updatedClient);
        Model expectedModel = new ModelManager(expectedAb, new UserPrefs());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_accumulatesExistingIntake_success() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>(), 2000, 300);
        ab.addPerson(trainer);
        ab.addPerson(client);
        Model model = new ModelManager(ab, new UserPrefs());

        int additionalCalories = 500;
        LogCalorieIntakeCommand command = new LogCalorieIntakeCommand(INDEX_FIRST_PERSON, additionalCalories);

        int expectedTotal = 800;
        Client updatedClient = new Client(client.getName(), client.getPhone(),
                client.getTrainerPhone(), client.getTrainerName(), client.getTags(),
                client.getCalorieTarget(), expectedTotal);
        String expectedMessage = String.format(LogCalorieIntakeCommand.MESSAGE_LOG_CALORIE_SUCCESS,
                updatedClient.getName(), additionalCalories, expectedTotal);

        AddressBook expectedAb = new AddressBook();
        expectedAb.addPerson(trainer);
        expectedAb.addPerson(updatedClient);
        Model expectedModel = new ModelManager(expectedAb, new UserPrefs());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_indexPointsToTrainer_throwsCommandException() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        ab.addPerson(trainer);
        Model model = new ModelManager(ab, new UserPrefs());

        LogCalorieIntakeCommand command = new LogCalorieIntakeCommand(INDEX_FIRST_PERSON, 500);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        ab.addPerson(trainer);
        Model model = new ModelManager(ab, new UserPrefs());

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredClientList().size() + 1);
        LogCalorieIntakeCommand command = new LogCalorieIntakeCommand(outOfBoundIndex, 500);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_indexInListPointsToNonClient_throwsCommandException() {
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());

        ModelStub modelStub = new ModelStub() {
            @Override
            public ObservableList<Person> getFilteredClientList() {
                return FXCollections.observableArrayList(trainer);
            }
        };

        LogCalorieIntakeCommand command = new LogCalorieIntakeCommand(INDEX_FIRST_PERSON, 500);
        assertThrows(CommandException.class, LogCalorieIntakeCommand.MESSAGE_NOT_A_CLIENT, () ->
                command.execute(modelStub));
    }

    @Test
    public void equals() {
        LogCalorieIntakeCommand commandA = new LogCalorieIntakeCommand(INDEX_FIRST_PERSON, 500);
        LogCalorieIntakeCommand commandB = new LogCalorieIntakeCommand(INDEX_SECOND_PERSON, 500);
        LogCalorieIntakeCommand commandC = new LogCalorieIntakeCommand(INDEX_FIRST_PERSON, 300);

        // same object -> returns true
        assertTrue(commandA.equals(commandA));

        // same values -> returns true
        assertTrue(commandA.equals(new LogCalorieIntakeCommand(INDEX_FIRST_PERSON, 500)));

        // different index -> returns false
        assertFalse(commandA.equals(commandB));

        // different calories -> returns false
        assertFalse(commandA.equals(commandC));

        // different types -> returns false
        assertFalse(commandA.equals(1));

        // null -> returns false
        assertFalse(commandA.equals(null));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        LogCalorieIntakeCommand command = new LogCalorieIntakeCommand(targetIndex, 500);
        String expected = LogCalorieIntakeCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + ", calories=500}";
        assertEquals(expected, command.toString());
    }
}
