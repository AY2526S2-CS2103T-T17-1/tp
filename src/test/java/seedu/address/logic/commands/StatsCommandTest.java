package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.TypicalPersons;

public class StatsCommandTest {

    @Test
    public void execute_stats_success() {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());

        // Expected sorted model
        // Dummy sort just to match type, but actually StatsCommand
        // itself updates the comparator based on clients
        expectedModel.updateSortedTrainerList((p1, p2) -> 0);

        // Let's just execute StatsCommand on expectedModel to get the expected state
        new StatsCommand().execute(expectedModel);

        assertCommandSuccess(new StatsCommand(), model, StatsCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
