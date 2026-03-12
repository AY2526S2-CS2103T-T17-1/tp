package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * ListClientsCommand.
 */
public class ListClientsCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listClientsUnfiltered_success() {
        expectedModel.updateFilteredPersonList(person -> person instanceof Client);
        assertCommandSuccess(new ListClientsCommand(), model,
                ListClientsCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listClientsAfterFilter_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        expectedModel.updateFilteredPersonList(person -> person instanceof Client);
        assertCommandSuccess(new ListClientsCommand(), model,
                ListClientsCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
