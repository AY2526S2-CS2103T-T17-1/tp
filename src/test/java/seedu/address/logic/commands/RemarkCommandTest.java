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
import seedu.address.model.person.Remark;
import seedu.address.model.person.Trainer;

public class RemarkCommandTest {

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

        Remark remark = new Remark("Recovering from ACL surgery");
        RemarkCommand command = new RemarkCommand(INDEX_FIRST_PERSON, remark);

        Client updatedClient = new Client(client.getName(), client.getPhone(),
                client.getTrainerPhone(), client.getTrainerName(), client.getTags(),
                client.getCalorieTarget(), client.getCalorieIntake(),
                client.getWorkoutFocus(), Optional.of(remark));

        String expectedMessage = String.format(RemarkCommand.MESSAGE_SUCCESS,
                updatedClient.getName(), remark.value);

        AddressBook expectedAb = new AddressBook();
        expectedAb.addPerson(trainer);
        expectedAb.addPerson(updatedClient);
        Model expectedModel = new ModelManager(expectedAb, new UserPrefs());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_overwritesExistingRemark_success() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        Remark oldRemark = new Remark("Old note");
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>(),
                0, 0, Optional.empty(), Optional.of(oldRemark));
        ab.addPerson(trainer);
        ab.addPerson(client);
        Model model = new ModelManager(ab, new UserPrefs());

        Remark newRemark = new Remark("New note");
        RemarkCommand command = new RemarkCommand(INDEX_FIRST_PERSON, newRemark);

        Client updatedClient = new Client(client.getName(), client.getPhone(),
                client.getTrainerPhone(), client.getTrainerName(), client.getTags(),
                client.getCalorieTarget(), client.getCalorieIntake(),
                client.getWorkoutFocus(), Optional.of(newRemark));
        String expectedMessage = String.format(RemarkCommand.MESSAGE_SUCCESS,
                updatedClient.getName(), newRemark.value);

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

        RemarkCommand command = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Note"));
        assertCommandFailure(command, model, RemarkCommand.MESSAGE_INVALID_CLIENT_INDEX);
    }

    @Test
    public void execute_invalidIndex_failure() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        ab.addPerson(trainer);
        Model model = new ModelManager(ab, new UserPrefs());

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredClientList().size() + 1);
        RemarkCommand command = new RemarkCommand(outOfBoundIndex, new Remark("Note"));
        assertCommandFailure(command, model, RemarkCommand.MESSAGE_INVALID_CLIENT_INDEX);
    }

    @Test
    public void execute_filteredClientListHasNonClient_failure() {
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        Model model = new ModelStubWithFilteredClientList(trainer);

        RemarkCommand command = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Note"));
        assertThrows(seedu.address.logic.commands.exceptions.CommandException.class,
                RemarkCommand.MESSAGE_INVALID_CLIENT_INDEX, () -> command.execute(model));
    }

    @Test
    public void toStringMethod() {
        RemarkCommand command = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Note"));
        String expected = RemarkCommand.class.getCanonicalName()
                + "{clientIndex=" + INDEX_FIRST_PERSON + ", remark=Note}";
        assertEquals(expected, command.toString());
    }

    @Test
    public void equals() {
        RemarkCommand command = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Note"));

        // same object -> returns true
        assertTrue(command.equals(command));

        // same values -> returns true
        RemarkCommand commandCopy = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Note"));
        assertTrue(command.equals(commandCopy));
        assertEquals(command.hashCode(), commandCopy.hashCode());

        // null -> returns false
        assertFalse(command.equals(null));

        // different type -> returns false
        assertFalse(command.equals(1));

        // different index -> returns false
        assertFalse(command.equals(new RemarkCommand(INDEX_SECOND_PERSON, new Remark("Note"))));

        // different remark -> returns false
        assertFalse(command.equals(new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Other"))));
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
