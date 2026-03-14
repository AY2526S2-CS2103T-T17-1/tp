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
import java.util.Optional;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
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
import seedu.address.model.person.WorkoutFocus;

public class SetFocusCommandTest {

    @Test
    public void execute_validClientIndex_success() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(client);
        Model model = new ModelManager(ab, new UserPrefs());

        WorkoutFocus focus = new WorkoutFocus("Chest");
        SetFocusCommand command = new SetFocusCommand(INDEX_FIRST_PERSON, focus);

        Client updatedClient = new Client(client.getName(), client.getPhone(),
                client.getTrainerPhone(), client.getTrainerName(), client.getTags(),
                client.getCalorieTarget(), client.getCalorieIntake(),
                Optional.of(focus), client.getRemark());

        String expectedMessage = String.format(SetFocusCommand.MESSAGE_SUCCESS,
                updatedClient.getName(), focus.value);

        AddressBook expectedAb = new AddressBook();
        expectedAb.addPerson(trainer);
        expectedAb.addPerson(updatedClient);
        Model expectedModel = new ModelManager(expectedAb, new UserPrefs());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_overwritesExistingFocus_success() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        WorkoutFocus oldFocus = new WorkoutFocus("Back");
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>(),
                0, 0, Optional.of(oldFocus), Optional.empty());
        ab.addPerson(trainer);
        ab.addPerson(client);
        Model model = new ModelManager(ab, new UserPrefs());

        WorkoutFocus newFocus = new WorkoutFocus("Chest");
        SetFocusCommand command = new SetFocusCommand(INDEX_FIRST_PERSON, newFocus);

        Client updatedClient = new Client(client.getName(), client.getPhone(),
                client.getTrainerPhone(), client.getTrainerName(), client.getTags(),
                client.getCalorieTarget(), client.getCalorieIntake(),
                Optional.of(newFocus), client.getRemark());
        String expectedMessage = String.format(SetFocusCommand.MESSAGE_SUCCESS,
                updatedClient.getName(), newFocus.value);

        AddressBook expectedAb = new AddressBook();
        expectedAb.addPerson(trainer);
        expectedAb.addPerson(updatedClient);
        Model expectedModel = new ModelManager(expectedAb, new UserPrefs());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_indexPointsToTrainer_failure() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        ab.addPerson(trainer);
        Model model = new ModelManager(ab, new UserPrefs());

        SetFocusCommand command = new SetFocusCommand(INDEX_FIRST_PERSON,
                new WorkoutFocus("Chest"));
        assertCommandFailure(command, model, SetFocusCommand.MESSAGE_INVALID_CLIENT_INDEX);
    }

    @Test
    public void execute_invalidIndex_failure() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        ab.addPerson(trainer);
        Model model = new ModelManager(ab, new UserPrefs());

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredClientList().size() + 1);
        SetFocusCommand command = new SetFocusCommand(outOfBoundIndex, new WorkoutFocus("Chest"));
        assertCommandFailure(command, model, SetFocusCommand.MESSAGE_INVALID_CLIENT_INDEX);
    }

    @Test
    public void execute_filteredClientListHasNonClient_failure() {
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        Model model = new ModelStubWithFilteredClientList(trainer);

        SetFocusCommand command = new SetFocusCommand(INDEX_FIRST_PERSON, new WorkoutFocus("Chest"));
        assertThrows(seedu.address.logic.commands.exceptions.CommandException.class,
                SetFocusCommand.MESSAGE_INVALID_CLIENT_INDEX, () -> command.execute(model));
    }

    @Test
    public void toStringMethod() {
        SetFocusCommand command = new SetFocusCommand(INDEX_FIRST_PERSON, new WorkoutFocus("Chest"));
        String expected = SetFocusCommand.class.getCanonicalName()
                + "{clientIndex=" + INDEX_FIRST_PERSON + ", workoutFocus=Chest}";
        assertEquals(expected, command.toString());
    }

    @Test
    public void equals() {
        SetFocusCommand command = new SetFocusCommand(INDEX_FIRST_PERSON, new WorkoutFocus("Chest"));

        // same object -> returns true
        assertTrue(command.equals(command));

        // same values -> returns true
        SetFocusCommand commandCopy = new SetFocusCommand(INDEX_FIRST_PERSON, new WorkoutFocus("Chest"));
        assertTrue(command.equals(commandCopy));
        assertEquals(command.hashCode(), commandCopy.hashCode());

        // null -> returns false
        assertFalse(command.equals(null));

        // different type -> returns false
        assertFalse(command.equals(1));

        // different index -> returns false
        assertFalse(command.equals(new SetFocusCommand(INDEX_SECOND_PERSON, new WorkoutFocus("Chest"))));

        // different focus -> returns false
        assertFalse(command.equals(new SetFocusCommand(INDEX_FIRST_PERSON, new WorkoutFocus("Back"))));
    }

    private static class ModelStubWithFilteredClientList extends ModelStub {
        private final ObservableList<Person> filteredClientList;

        ModelStubWithFilteredClientList(Person person) {
            filteredClientList = FXCollections.observableArrayList(person);
        }

        @Override
        public ObservableList<Person> getFilteredClientList() {
            return filteredClientList;
        }
    }
}
