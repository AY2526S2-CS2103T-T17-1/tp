package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
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
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredTrainerList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.TargetType.TRAINER, INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TRAINER_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTrainerList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.TargetType.TRAINER, outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexUnfilteredClientList_success() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
            new Email("john@example.com"), new HashSet<>());
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
            trainer.getPhone(), trainer.getName(), new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(client);
        Model model = new ModelManager(ab, new UserPrefs());

        Person personToDelete = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.TargetType.CLIENT, INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_CLIENT_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredClientList_throwsCommandException() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
            new Email("john@example.com"), new HashSet<>());
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
            trainer.getPhone(), trainer.getName(), new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(client);
        Model model = new ModelManager(ab, new UserPrefs());

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredClientList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.TargetType.CLIENT, outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredTrainerList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.TargetType.TRAINER, INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_TRAINER_SUCCESS,
                Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.TargetType.TRAINER, outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_deletingTrainerWithActiveClients_throwsCommandException() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(client);
        Model model = new ModelManager(ab, new UserPrefs());

        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.TargetType.TRAINER, INDEX_FIRST_PERSON);

        assertCommandFailure(deleteCommand, model, DeleteCommand.MESSAGE_TRAINER_HAS_CLIENTS);
    }

    @Test
    public void execute_trainerTargetButNotTrainer_throwsCommandException() {
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
                new Phone("91234567"), new Name("John"), new HashSet<>());
        Model model = new ModelStubWithFilteredTrainerList(client);

        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.TargetType.TRAINER, INDEX_FIRST_PERSON);
        assertThrows(seedu.address.logic.commands.exceptions.CommandException.class,
                DeleteCommand.MESSAGE_NOT_A_TRAINER, () -> deleteCommand.execute(model));
    }

    @Test
    public void execute_clientTargetButNotClient_throwsCommandException() {
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        Model model = new ModelStubWithFilteredClientList(trainer);

        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.TargetType.CLIENT, INDEX_FIRST_PERSON);
        assertThrows(seedu.address.logic.commands.exceptions.CommandException.class,
                DeleteCommand.MESSAGE_NOT_A_CLIENT, () -> deleteCommand.execute(model));
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(DeleteCommand.TargetType.TRAINER, INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(DeleteCommand.TargetType.TRAINER, INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(DeleteCommand.TargetType.TRAINER, INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(DeleteCommand.TargetType.TRAINER, targetIndex);
        String expected = DeleteCommand.class.getCanonicalName()
                + "{targetType=TRAINER, targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    private static class ModelStubWithFilteredTrainerList extends ModelStub {
        private final ObservableList<Person> filteredTrainerList;

        ModelStubWithFilteredTrainerList(Person person) {
            filteredTrainerList = FXCollections.observableArrayList(person);
        }

        @Override
        public ObservableList<Person> getFilteredTrainerList() {
            return filteredTrainerList;
        }
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
