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
 * {@code DeleteTrainerCommand}.
 */
public class DeleteTrainerCommandTest {

    @Test
    public void execute_validIndexTrainerNoClients_success() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        ab.addPerson(trainer);
        Model model = new ModelManager(ab, new UserPrefs());

        DeleteTrainerCommand deleteTrainerCommand = new DeleteTrainerCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteTrainerCommand.MESSAGE_DELETE_TRAINER_SUCCESS,
                Messages.format(trainer));

        ModelManager expectedModel = new ModelManager(ab, new UserPrefs());
        expectedModel.deletePerson(trainer);

        assertCommandSuccess(deleteTrainerCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_trainerHasActiveClients_throwsCommandException() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(client);
        Model model = new ModelManager(ab, new UserPrefs());

        DeleteTrainerCommand deleteTrainerCommand = new DeleteTrainerCommand(INDEX_FIRST_PERSON);

        assertCommandFailure(deleteTrainerCommand, model, DeleteTrainerCommand.MESSAGE_TRAINER_HAS_CLIENTS);
    }

    @Test
    public void execute_indexPointsToClient_throwsCommandException() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(client);
        Model model = new ModelManager(ab, new UserPrefs());

        // Trainer list contains only trainers; index 2 is out of bounds here.
        DeleteTrainerCommand deleteTrainerCommand = new DeleteTrainerCommand(INDEX_SECOND_PERSON);

        assertCommandFailure(deleteTrainerCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        ab.addPerson(trainer);
        Model model = new ModelManager(ab, new UserPrefs());

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTrainerList().size() + 1);
        DeleteTrainerCommand deleteTrainerCommand = new DeleteTrainerCommand(outOfBoundIndex);

        assertCommandFailure(deleteTrainerCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_indexInListPointsToNonTrainer_throwsCommandException() {
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
                new Phone("91234567"), new Name("John"), new HashSet<>());

        ModelStub modelStub = new ModelStub() {
            @Override
            public ObservableList<Person> getFilteredTrainerList() {
                return FXCollections.observableArrayList(client);
            }
        };

        DeleteTrainerCommand deleteTrainerCommand = new DeleteTrainerCommand(INDEX_FIRST_PERSON);
        assertThrows(CommandException.class, DeleteTrainerCommand.MESSAGE_NOT_A_TRAINER, () ->
            deleteTrainerCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        DeleteTrainerCommand deleteFirstCommand = new DeleteTrainerCommand(INDEX_FIRST_PERSON);
        DeleteTrainerCommand deleteSecondCommand = new DeleteTrainerCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteTrainerCommand deleteFirstCommandCopy = new DeleteTrainerCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteTrainerCommand deleteTrainerCommand = new DeleteTrainerCommand(targetIndex);
        String expected = DeleteTrainerCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteTrainerCommand.toString());
    }
}
