package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

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
public class ListClientsCommandTest {

    @Test
    public void execute_listIsFiltered_showsAllClients() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("Trainer"), new Phone("90000000"), new Email("t@example.com"),
                new HashSet<>());
        Client clientA = new Client(new Name("Alice"), new Phone("80000001"), trainer.getPhone(), trainer.getName(),
                new HashSet<>());
        Client clientB = new Client(new Name("Bob"), new Phone("80000002"), trainer.getPhone(), trainer.getName(),
                new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(clientA);
        ab.addPerson(clientB);

        Model model = new ModelManager(ab, new UserPrefs());
        model.updateFilteredClientList(new NameContainsKeywordsPredicate(List.of("Alice")));

        Model expectedModel = new ModelManager(ab, new UserPrefs());
        expectedModel.updateFilteredClientList(Model.PREDICATE_SHOW_ALL_PERSONS);

        ListClientsCommand command = new ListClientsCommand();
        assertCommandSuccess(command, model, ListClientsCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(2, model.getFilteredClientList().size());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        ListClientsCommand command = new ListClientsCommand();
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }
}
