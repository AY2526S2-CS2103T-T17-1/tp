package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelStub;
import seedu.address.model.person.Person;
import seedu.address.model.person.Trainer;
import seedu.address.testutil.PersonBuilder;

public class AddTrainerCommandTest {

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddTrainerCommand(null));
    }

    @Test
    public void execute_duplicateTrainer_throwsCommandException() {
        Trainer alice = (Trainer) new PersonBuilder().withName("Alice").withEmail("alice@gmail.com").build();
        AddTrainerCommand command = new AddTrainerCommand(alice);

        ModelStub modelStub = new ModelStub() {
            @Override
            public boolean hasPerson(Person person) {
                return true;
            }
        };

        assertThrows(CommandException.class, AddTrainerCommand.MESSAGE_DUPLICATE_TRAINER, () ->
            command.execute(modelStub));
    }

    @Test
    public void equals() {
        Trainer alice = (Trainer) new PersonBuilder().withName("Alice").withEmail("alice@gmail.com").build();
        Trainer bob = (Trainer) new PersonBuilder().withName("Bob").withEmail("bob@gmail.com").build();
        AddTrainerCommand addAliceCommand = new AddTrainerCommand(alice);
        AddTrainerCommand addBobCommand = new AddTrainerCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddTrainerCommand addAliceCommandCopy = new AddTrainerCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }
}
