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
 * {@code SetCalorieTargetCommand}.
 */
public class SetCalorieTargetCommandTest {

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

        int targetCalories = 2000;
        // Client is at index 1 (1-based) in the client list
        SetCalorieTargetCommand command = new SetCalorieTargetCommand(INDEX_FIRST_PERSON, targetCalories);

        Client updatedClient = new Client(client.getName(), client.getPhone(),
                client.getTrainerPhone(), client.getTrainerName(), client.getTags(),
                targetCalories, client.getCalorieIntake());
        String expectedMessage = String.format(SetCalorieTargetCommand.MESSAGE_SET_CALORIE_TARGET_SUCCESS,
                updatedClient.getName(), targetCalories);

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

        SetCalorieTargetCommand command = new SetCalorieTargetCommand(INDEX_FIRST_PERSON, 2000);

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
        SetCalorieTargetCommand command = new SetCalorieTargetCommand(outOfBoundIndex, 2000);

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

        SetCalorieTargetCommand command = new SetCalorieTargetCommand(INDEX_FIRST_PERSON, 2000);
        assertThrows(CommandException.class, SetCalorieTargetCommand.MESSAGE_NOT_A_CLIENT, () ->
                command.execute(modelStub));
    }

    @Test
    public void execute_overwritesExistingTarget_success() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>(), 1500, 300);
        ab.addPerson(trainer);
        ab.addPerson(client);
        Model model = new ModelManager(ab, new UserPrefs());

        int newTarget = 2500;
        SetCalorieTargetCommand command = new SetCalorieTargetCommand(INDEX_FIRST_PERSON, newTarget);

        // Intake should be preserved; only target is updated
        Client updatedClient = new Client(client.getName(), client.getPhone(),
                client.getTrainerPhone(), client.getTrainerName(), client.getTags(),
                newTarget, client.getCalorieIntake());
        String expectedMessage = String.format(SetCalorieTargetCommand.MESSAGE_SET_CALORIE_TARGET_SUCCESS,
                updatedClient.getName(), newTarget);

        AddressBook expectedAb = new AddressBook();
        expectedAb.addPerson(trainer);
        expectedAb.addPerson(updatedClient);
        Model expectedModel = new ModelManager(expectedAb, new UserPrefs());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        SetCalorieTargetCommand commandA = new SetCalorieTargetCommand(INDEX_FIRST_PERSON, 2000);
        SetCalorieTargetCommand commandB = new SetCalorieTargetCommand(INDEX_SECOND_PERSON, 2000);
        SetCalorieTargetCommand commandC = new SetCalorieTargetCommand(INDEX_FIRST_PERSON, 1500);

        // same object -> returns true
        assertTrue(commandA.equals(commandA));

        // same values -> returns true
        assertTrue(commandA.equals(new SetCalorieTargetCommand(INDEX_FIRST_PERSON, 2000)));

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
        SetCalorieTargetCommand command = new SetCalorieTargetCommand(targetIndex, 2000);
        String expected = SetCalorieTargetCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + ", calorieTarget=2000}";
        assertEquals(expected, command.toString());
    }
}
