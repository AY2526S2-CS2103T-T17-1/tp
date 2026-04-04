package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditTrainerCommand.EditTrainerDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;

public class EditTrainerCommandTest {

    private Trainer sampleTrainer() {
        return new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), Set.of());
    }

    private Model modelWithTrainer() {
        AddressBook ab = new AddressBook();
        ab.addPerson(sampleTrainer());
        return new ModelManager(ab, new UserPrefs());
    }

    // ==================== Constructor tests ====================

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        EditTrainerDescriptor descriptor = new EditTrainerDescriptor();
        descriptor.setName(new Name("John"));
        assertThrows(NullPointerException.class, ()
                -> new EditTrainerCommand(null, descriptor));
    }

    @Test
    public void constructor_nullDescriptor_throwsNullPointerException() {
        assertThrows(NullPointerException.class, ()
                -> new EditTrainerCommand(Index.fromOneBased(1), null));
    }

    // ==================== Execute tests ====================

    @Test
    public void execute_allFieldsSpecified_success() {
        Model model = modelWithTrainer();

        EditTrainerDescriptor descriptor = new EditTrainerDescriptor();
        descriptor.setName(new Name("Jane Doe"));
        descriptor.setPhone(new Phone("99999999"));
        descriptor.setEmail(new Email("jane@example.com"));

        Trainer editedTrainer = new Trainer(
                new Name("Jane Doe"), new Phone("99999999"),
                new Email("jane@example.com"), Set.of());

        EditTrainerCommand command = new EditTrainerCommand(
                Index.fromOneBased(1), descriptor);

        String expectedMessage = String.format(
                EditTrainerCommand.MESSAGE_EDIT_TRAINER_SUCCESS,
                Messages.format(editedTrainer));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(
                model.getFilteredTrainerList().get(0), editedTrainer);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nameFieldOnly_success() {
        Model model = modelWithTrainer();

        EditTrainerDescriptor descriptor = new EditTrainerDescriptor();
        descriptor.setName(new Name("Mike"));

        Trainer editedTrainer = new Trainer(
                new Name("Mike"), new Phone("91234567"),
                new Email("john@example.com"), Set.of());

        EditTrainerCommand command = new EditTrainerCommand(
                Index.fromOneBased(1), descriptor);

        String expectedMessage = String.format(
                EditTrainerCommand.MESSAGE_EDIT_TRAINER_SUCCESS,
                Messages.format(editedTrainer));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(
                model.getFilteredTrainerList().get(0), editedTrainer);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_phoneFieldOnly_success() {
        Model model = modelWithTrainer();

        EditTrainerDescriptor descriptor = new EditTrainerDescriptor();
        descriptor.setPhone(new Phone("88888888"));

        Trainer editedTrainer = new Trainer(
                new Name("John"), new Phone("88888888"),
                new Email("john@example.com"), Set.of());

        EditTrainerCommand command = new EditTrainerCommand(
                Index.fromOneBased(1), descriptor);

        String expectedMessage = String.format(
                EditTrainerCommand.MESSAGE_EDIT_TRAINER_SUCCESS,
                Messages.format(editedTrainer));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(
                model.getFilteredTrainerList().get(0), editedTrainer);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_emailFieldOnly_success() {
        Model model = modelWithTrainer();

        EditTrainerDescriptor descriptor = new EditTrainerDescriptor();
        descriptor.setEmail(new Email("newemail@example.com"));

        Trainer editedTrainer = new Trainer(
                new Name("John"), new Phone("91234567"),
                new Email("newemail@example.com"), Set.of());

        EditTrainerCommand command = new EditTrainerCommand(
                Index.fromOneBased(1), descriptor);

        String expectedMessage = String.format(
                EditTrainerCommand.MESSAGE_EDIT_TRAINER_SUCCESS,
                Messages.format(editedTrainer));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(
                model.getFilteredTrainerList().get(0), editedTrainer);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidTrainerIndex_failure() {
        Model model = modelWithTrainer();

        EditTrainerDescriptor descriptor = new EditTrainerDescriptor();
        descriptor.setName(new Name("Nobody"));

        EditTrainerCommand command = new EditTrainerCommand(
                Index.fromOneBased(99), descriptor);

        assertCommandFailure(command, model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateTrainer_failure() {
        AddressBook ab = new AddressBook();
        Trainer john = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), Set.of());
        Trainer jane = new Trainer(new Name("Jane"), new Phone("92222222"),
                new Email("jane@example.com"), Set.of());
        ab.addPerson(john);
        ab.addPerson(jane);
        Model model = new ModelManager(ab, new UserPrefs());

        // Edit Jane to have John's phone AND a new email so
        // isSamePerson(original) returns false (different phone, email)
        // but hasPerson matches John (same phone).
        EditTrainerDescriptor descriptor = new EditTrainerDescriptor();
        descriptor.setPhone(new Phone("91234567"));
        descriptor.setEmail(new Email("newemail@example.com"));

        EditTrainerCommand command = new EditTrainerCommand(
                Index.fromOneBased(2), descriptor);

        assertCommandFailure(command, model,
                EditTrainerCommand.MESSAGE_DUPLICATE_TRAINER);
    }

    // ==================== EditTrainerDescriptor tests ===========

    @Test
    public void editTrainerDescriptor_noFieldEdited_returnsFalse() {
        EditTrainerDescriptor descriptor = new EditTrainerDescriptor();
        assertFalse(descriptor.isAnyFieldEdited());
    }

    @Test
    public void editTrainerDescriptor_oneFieldEdited_returnsTrue() {
        EditTrainerDescriptor descriptor = new EditTrainerDescriptor();
        descriptor.setEmail(new Email("test@test.com"));
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void editTrainerDescriptor_copyConstructor() {
        EditTrainerDescriptor original = new EditTrainerDescriptor();
        original.setName(new Name("Alice"));
        original.setPhone(new Phone("99999999"));
        original.setEmail(new Email("alice@test.com"));

        EditTrainerDescriptor copy = new EditTrainerDescriptor(original);
        assertEquals(original, copy);
    }

    // ==================== Equals tests =========================

    @Test
    public void equals() {
        EditTrainerDescriptor descriptorA = new EditTrainerDescriptor();
        descriptorA.setName(new Name("Alice"));
        EditTrainerCommand commandA = new EditTrainerCommand(
                Index.fromOneBased(1), descriptorA);

        EditTrainerDescriptor descriptorB = new EditTrainerDescriptor();
        descriptorB.setName(new Name("Bob"));
        EditTrainerCommand commandB = new EditTrainerCommand(
                Index.fromOneBased(1), descriptorB);

        // same object → true
        assertTrue(commandA.equals(commandA));

        // same values → true
        EditTrainerDescriptor descriptorACopy =
                new EditTrainerDescriptor();
        descriptorACopy.setName(new Name("Alice"));
        EditTrainerCommand commandACopy = new EditTrainerCommand(
                Index.fromOneBased(1), descriptorACopy);
        assertTrue(commandA.equals(commandACopy));

        // different types → false
        assertFalse(commandA.equals(1));

        // null → false
        assertFalse(commandA.equals(null));

        // different descriptor → false
        assertFalse(commandA.equals(commandB));

        // different index → false
        EditTrainerCommand commandDiffIndex = new EditTrainerCommand(
                Index.fromOneBased(2), descriptorA);
        assertFalse(commandA.equals(commandDiffIndex));
    }

    @Test
    public void editTrainerDescriptor_equals() {
        EditTrainerDescriptor descriptorA = new EditTrainerDescriptor();
        descriptorA.setName(new Name("Alice"));
        descriptorA.setEmail(new Email("alice@test.com"));

        // same object
        assertTrue(descriptorA.equals(descriptorA));

        // same values
        EditTrainerDescriptor descriptorACopy =
                new EditTrainerDescriptor();
        descriptorACopy.setName(new Name("Alice"));
        descriptorACopy.setEmail(new Email("alice@test.com"));
        assertTrue(descriptorA.equals(descriptorACopy));

        // null
        assertFalse(descriptorA.equals(null));

        // different type
        assertFalse(descriptorA.equals("string"));

        // different name
        EditTrainerDescriptor descriptorB = new EditTrainerDescriptor();
        descriptorB.setName(new Name("Bob"));
        descriptorB.setEmail(new Email("alice@test.com"));
        assertFalse(descriptorA.equals(descriptorB));
    }
}
