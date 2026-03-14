package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;

public class AddClientCommandTest {

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddClientCommand(null,
                ALICE.getPhone(), Index.fromOneBased(1)));
    }

    @Test
    public void equals() {
        AddClientCommand addAliceCommand = new AddClientCommand(ALICE.getName(),
                ALICE.getPhone(), Index.fromOneBased(1));
        AddClientCommand addBobCommand = new AddClientCommand(ALICE.getName(),
                ALICE.getPhone(), Index.fromOneBased(2));

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddClientCommand addAliceCommandCopy = new AddClientCommand(ALICE.getName(),
                ALICE.getPhone(), Index.fromOneBased(1));
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different trainer index -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void execute_validTrainerIndexAddsClient_success() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), Set.of());
        ab.addPerson(trainer);

        Model model = new ModelManager(ab, new UserPrefs());

        Name clientName = new Name("Alice");
        Phone clientPhone = new Phone("81234567");
        AddClientCommand command = new AddClientCommand(clientName, clientPhone, Index.fromOneBased(1));

        Client expectedClient = new Client(clientName, clientPhone, trainer.getPhone(), trainer.getName(),
            new HashSet<>());
        String expectedMessage = String.format(AddClientCommand.MESSAGE_SUCCESS,
                expectedClient.getName(), trainer.getName());

        Model expectedModel = new ModelManager(ab, new UserPrefs());
        expectedModel.addPerson(expectedClient);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidTrainerIndex_throwsCommandException() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), Set.of());
        ab.addPerson(trainer);
        Model model = new ModelManager(ab, new UserPrefs());

        AddClientCommand command = new AddClientCommand(new Name("Alice"), new Phone("81234567"),
                Index.fromOneBased(2));
        assertCommandFailure(command, model, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateClient_throwsCommandException() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), Set.of());
        Client existingClient = new Client(new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(existingClient);
        Model model = new ModelManager(ab, new UserPrefs());

        AddClientCommand command = new AddClientCommand(existingClient.getName(), existingClient.getPhone(),
                Index.fromOneBased(1));
        assertCommandFailure(command, model, AddClientCommand.MESSAGE_DUPLICATE_CLIENT);
    }

    @Test
    public void execute_targetPersonNotTrainer_throwsCommandException() {
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
                new Phone("99999999"), new Name("Ghost"), new HashSet<>());
        Model model = new ModelStubWithTrainerList(FXCollections.observableArrayList(client));

        AddClientCommand command = new AddClientCommand(new Name("Bob"), new Phone("82223333"),
                Index.fromOneBased(1));
        assertThrows(CommandException.class, AddClientCommand.MESSAGE_INVALID_TRAINER, () -> command.execute(model));
    }

    private static class ModelStub implements Model {
        @Override
        public boolean hasClientWithTrainer(Trainer trainer) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            return false;
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public ObservableList<Person> getFilteredTrainerList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public ObservableList<Person> getFilteredClientList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredTrainerList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredClientList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSelectedTrainer(Trainer trainer) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void clearSelectedTrainer() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Optional<Trainer> getSelectedTrainer() {
            return Optional.empty();
        }
    }

    private static class ModelStubWithTrainerList extends ModelStub {
        private final ObservableList<Person> trainerList;

        private ModelStubWithTrainerList(ObservableList<Person> trainerList) {
            this.trainerList = trainerList;
        }

        @Override
        public ObservableList<Person> getFilteredTrainerList() {
            return trainerList;
        }
    }
}
