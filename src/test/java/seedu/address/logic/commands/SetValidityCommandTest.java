package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;
import seedu.address.model.person.Validity;

public class SetValidityCommandTest {

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

        Validity validity = new Validity("2026-12-31");
        SetValidityCommand command = new SetValidityCommand(INDEX_FIRST_PERSON, validity);

        Client updatedClient = new Client(client.getName(), client.getPhone(),
                client.getTrainerPhone(), client.getTrainerName(), client.getTags(),
                client.getCalorieTarget(), client.getCalorieIntake(),
                client.getWorkoutFocus(), client.getRemark(), Optional.of(validity));

        String expectedMessage = String.format(SetValidityCommand.MESSAGE_SET_VALIDITY_SUCCESS,
                validity.toString(), updatedClient.getName());

        AddressBook expectedAb = new AddressBook();
        expectedAb.addPerson(trainer);
        expectedAb.addPerson(updatedClient);
        Model expectedModel = new ModelManager(expectedAb, new UserPrefs());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_doesNotResetClientFilter() throws Exception {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        Client alice = new Client(new Name("Alice"), new Phone("81234567"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        Client bob = new Client(new Name("Bob"), new Phone("82345678"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(alice);
        ab.addPerson(bob);
        Model model = new ModelManager(ab, new UserPrefs());

        // Apply a filter so only Alice is visible
        model.updateFilteredClientList(new NameContainsKeywordsPredicate(List.of("Alice")));
        assertEquals(1, model.getFilteredClientList().size());

        // Run set-validity on the first (and only) visible client
        new SetValidityCommand(INDEX_FIRST_PERSON, new Validity("2026-12-31")).execute(model);

        // Filter must still be in place — list should still show only 1 client
        assertEquals(1, model.getFilteredClientList().size());
        assertEquals("Alice", model.getFilteredClientList().get(0).getName().getFullName());
    }

    @Test
    public void execute_invalidIndex_failure() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("John"), new Phone("91234567"),
                new Email("john@example.com"), new HashSet<>());
        ab.addPerson(trainer);
        Model model = new ModelManager(ab, new UserPrefs());

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredClientList().size() + 1);
        SetValidityCommand command = new SetValidityCommand(outOfBoundIndex, new Validity("2026-12-31"));
        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        SetValidityCommand command = new SetValidityCommand(INDEX_FIRST_PERSON, new Validity("2026-12-31"));

        // same object -> returns true
        assertTrue(command.equals(command));

        // same values -> returns true
        SetValidityCommand commandCopy = new SetValidityCommand(INDEX_FIRST_PERSON, new Validity("2026-12-31"));
        assertTrue(command.equals(commandCopy));

        // null -> returns false
        assertFalse(command.equals(null));

        // different type -> returns false
        assertFalse(command.equals(1));

        // different index -> returns false
        assertFalse(command.equals(new SetValidityCommand(INDEX_SECOND_PERSON, new Validity("2026-12-31"))));

        // different validity -> returns false
        assertFalse(command.equals(new SetValidityCommand(INDEX_FIRST_PERSON, new Validity("2027-12-31"))));
    }
}
