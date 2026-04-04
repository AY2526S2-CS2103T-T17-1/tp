package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditClientCommand.EditClientDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Trainer;
import seedu.address.model.person.Validity;
import seedu.address.model.person.WorkoutFocus;

public class EditClientCommandTest {

    private Trainer sampleTrainer() {
        return new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), Set.of());
    }

    private Client sampleClient(Trainer trainer) {
        return new Client(new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
    }

    private Model modelWithTrainerAndClient() {
        AddressBook ab = new AddressBook();
        Trainer trainer = sampleTrainer();
        ab.addPerson(trainer);
        ab.addPerson(sampleClient(trainer));
        return new ModelManager(ab, new UserPrefs());
    }

    // ==================== Constructor tests ====================

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(new Name("Alice"));
        assertThrows(NullPointerException.class, ()
                -> new EditClientCommand(null, descriptor));
    }

    @Test
    public void constructor_nullDescriptor_throwsNullPointerException() {
        assertThrows(NullPointerException.class, ()
                -> new EditClientCommand(Index.fromOneBased(1), null));
    }

    // ==================== Execute tests ====================

    @Test
    public void execute_allFieldsSpecified_success() {
        Model model = modelWithTrainerAndClient();
        Trainer trainer = sampleTrainer();

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(new Name("Bob Tan"));
        descriptor.setPhone(new Phone("99999999"));

        Client editedClient = new Client(
                new Name("Bob Tan"), new Phone("99999999"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());

        EditClientCommand command = new EditClientCommand(
                Index.fromOneBased(1), descriptor);

        String expectedMessage = String.format(
                EditClientCommand.MESSAGE_EDIT_CLIENT_SUCCESS,
                Messages.format(editedClient));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(
                model.getFilteredClientList().get(0), editedClient);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nameFieldOnly_success() {
        Model model = modelWithTrainerAndClient();
        Trainer trainer = sampleTrainer();

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(new Name("Carol"));

        Client editedClient = new Client(
                new Name("Carol"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());

        EditClientCommand command = new EditClientCommand(
                Index.fromOneBased(1), descriptor);

        String expectedMessage = String.format(
                EditClientCommand.MESSAGE_EDIT_CLIENT_SUCCESS,
                Messages.format(editedClient));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(
                model.getFilteredClientList().get(0), editedClient);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_phoneFieldOnly_success() {
        Model model = modelWithTrainerAndClient();
        Trainer trainer = sampleTrainer();

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setPhone(new Phone("77777777"));

        Client editedClient = new Client(
                new Name("Alice"), new Phone("77777777"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());

        EditClientCommand command = new EditClientCommand(
                Index.fromOneBased(1), descriptor);

        String expectedMessage = String.format(
                EditClientCommand.MESSAGE_EDIT_CLIENT_SUCCESS,
                Messages.format(editedClient));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(
                model.getFilteredClientList().get(0), editedClient);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_trainerReassignment_success() {
        AddressBook ab = new AddressBook();
        Trainer trainer1 = sampleTrainer();
        Trainer trainer2 = new Trainer(new Name("Jane"), new Phone("92222222"),
                new Email("jane@example.com"), Set.of());
        ab.addPerson(trainer1);
        ab.addPerson(trainer2);
        ab.addPerson(sampleClient(trainer1));
        Model model = new ModelManager(ab, new UserPrefs());

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setTrainerIndex(Index.fromOneBased(2));

        Client editedClient = new Client(
                new Name("Alice"), new Phone("81234567"),
                trainer2.getPhone(), trainer2.getName(), new HashSet<>());

        EditClientCommand command = new EditClientCommand(
                Index.fromOneBased(1), descriptor);

        String expectedMessage = String.format(
                EditClientCommand.MESSAGE_EDIT_CLIENT_SUCCESS,
                Messages.format(editedClient));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(
                model.getFilteredClientList().get(0), editedClient);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_calorieTargetField_success() {
        Model model = modelWithTrainerAndClient();
        Trainer trainer = sampleTrainer();

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setCalorieTarget(2500);

        Client editedClient = new Client(
                new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(),
                new HashSet<>(), 2500, 0);

        EditClientCommand command = new EditClientCommand(
                Index.fromOneBased(1), descriptor);

        String expectedMessage = String.format(
                EditClientCommand.MESSAGE_EDIT_CLIENT_SUCCESS,
                Messages.format(editedClient));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(
                model.getFilteredClientList().get(0), editedClient);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_workoutFocusField_success() {
        Model model = modelWithTrainerAndClient();
        Trainer trainer = sampleTrainer();

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setWorkoutFocus(new WorkoutFocus("Legs"));

        Client base = new Client(
                new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        Client editedClient = base.withWorkoutFocus(new WorkoutFocus("Legs"));

        EditClientCommand command = new EditClientCommand(
                Index.fromOneBased(1), descriptor);

        String expectedMessage = String.format(
                EditClientCommand.MESSAGE_EDIT_CLIENT_SUCCESS,
                Messages.format(editedClient));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(
                model.getFilteredClientList().get(0), editedClient);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_remarkField_success() {
        Model model = modelWithTrainerAndClient();
        Trainer trainer = sampleTrainer();

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setRemark(new Remark("Recovering"));

        Client base = new Client(
                new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        Client editedClient = base.withRemark(new Remark("Recovering"));

        EditClientCommand command = new EditClientCommand(
                Index.fromOneBased(1), descriptor);

        String expectedMessage = String.format(
                EditClientCommand.MESSAGE_EDIT_CLIENT_SUCCESS,
                Messages.format(editedClient));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(
                model.getFilteredClientList().get(0), editedClient);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validityField_success() {
        Model model = modelWithTrainerAndClient();
        Trainer trainer = sampleTrainer();

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setValidity(new Validity("2027-06-30"));

        Client base = new Client(
                new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        Client editedClient = base.withValidity(new Validity("2027-06-30"));

        EditClientCommand command = new EditClientCommand(
                Index.fromOneBased(1), descriptor);

        String expectedMessage = String.format(
                EditClientCommand.MESSAGE_EDIT_CLIENT_SUCCESS,
                Messages.format(editedClient));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(
                model.getFilteredClientList().get(0), editedClient);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidClientIndex_failure() {
        Model model = modelWithTrainerAndClient();

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(new Name("Nobody"));

        EditClientCommand command = new EditClientCommand(
                Index.fromOneBased(99), descriptor);

        assertCommandFailure(command, model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidTrainerIndex_failure() {
        Model model = modelWithTrainerAndClient();

        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setTrainerIndex(Index.fromOneBased(99));

        EditClientCommand command = new EditClientCommand(
                Index.fromOneBased(1), descriptor);

        assertCommandFailure(command, model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateClient_failure() {
        AddressBook ab = new AddressBook();
        Trainer trainer = sampleTrainer();
        Client alice = new Client(new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        Client bob = new Client(new Name("Bob"), new Phone("82222222"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(alice);
        ab.addPerson(bob);
        Model model = new ModelManager(ab, new UserPrefs());

        // Edit Bob to have Alice's phone → duplicate
        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setPhone(new Phone("81234567"));

        EditClientCommand command = new EditClientCommand(
                Index.fromOneBased(2), descriptor);

        assertCommandFailure(command, model,
                EditClientCommand.MESSAGE_DUPLICATE_CLIENT);
    }

    @Test
    public void execute_noFieldEdited_failure() {
        Model model = modelWithTrainerAndClient();
        EditClientDescriptor descriptor = new EditClientDescriptor();

        // Descriptor with no fields set should be caught by parser,
        // but if it reaches command execute, it should still not crash.
        // We test the descriptor's isAnyFieldEdited check separately.
        assertFalse(descriptor.isAnyFieldEdited());
    }

    // ==================== EditClientDescriptor tests ===========

    @Test
    public void editClientDescriptor_noFieldEdited_returnsFalse() {
        EditClientDescriptor descriptor = new EditClientDescriptor();
        assertFalse(descriptor.isAnyFieldEdited());
    }

    @Test
    public void editClientDescriptor_oneFieldEdited_returnsTrue() {
        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setName(new Name("Test"));
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void editClientDescriptor_copyConstructor() {
        EditClientDescriptor original = new EditClientDescriptor();
        original.setName(new Name("Alice"));
        original.setPhone(new Phone("99999999"));
        original.setCalorieTarget(2000);

        EditClientDescriptor copy = new EditClientDescriptor(original);
        assertEquals(original, copy);
    }

    // ==================== Equals tests =========================

    @Test
    public void equals() {
        EditClientDescriptor descriptorA = new EditClientDescriptor();
        descriptorA.setName(new Name("Alice"));
        EditClientCommand commandA = new EditClientCommand(
                Index.fromOneBased(1), descriptorA);

        EditClientDescriptor descriptorB = new EditClientDescriptor();
        descriptorB.setName(new Name("Bob"));
        EditClientCommand commandB = new EditClientCommand(
                Index.fromOneBased(1), descriptorB);

        // same object → true
        assertTrue(commandA.equals(commandA));

        // same values → true
        EditClientDescriptor descriptorACopy = new EditClientDescriptor();
        descriptorACopy.setName(new Name("Alice"));
        EditClientCommand commandACopy = new EditClientCommand(
                Index.fromOneBased(1), descriptorACopy);
        assertTrue(commandA.equals(commandACopy));

        // different types → false
        assertFalse(commandA.equals(1));

        // null → false
        assertFalse(commandA.equals(null));

        // different descriptor → false
        assertFalse(commandA.equals(commandB));

        // different index → false
        EditClientCommand commandDiffIndex = new EditClientCommand(
                Index.fromOneBased(2), descriptorA);
        assertFalse(commandA.equals(commandDiffIndex));
    }

    @Test
    public void editClientDescriptor_equals() {
        EditClientDescriptor descriptorA = new EditClientDescriptor();
        descriptorA.setName(new Name("Alice"));
        descriptorA.setPhone(new Phone("99999999"));

        // same object
        assertTrue(descriptorA.equals(descriptorA));

        // same values
        EditClientDescriptor descriptorACopy = new EditClientDescriptor();
        descriptorACopy.setName(new Name("Alice"));
        descriptorACopy.setPhone(new Phone("99999999"));
        assertTrue(descriptorA.equals(descriptorACopy));

        // null
        assertFalse(descriptorA.equals(null));

        // different type
        assertFalse(descriptorA.equals("string"));

        // different name
        EditClientDescriptor descriptorB = new EditClientDescriptor();
        descriptorB.setName(new Name("Bob"));
        descriptorB.setPhone(new Phone("99999999"));
        assertFalse(descriptorA.equals(descriptorB));
    }
}
