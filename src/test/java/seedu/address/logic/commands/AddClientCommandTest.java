package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;

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
}
