package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.\n\n"
            + "Available commands:\n"
            + "  add-trainer   — Add a new trainer\n"
            + "  add-client    — Add a new client assigned to a trainer\n"
            + "  delete-trainer — Delete a trainer\n"
            + "  delete-client  — Delete a client\n"
            + "  list          — List all persons\n"
            + "  list-trainers — List all trainers\n"
            + "  list-clients  — List all clients\n"
            + "  find          — Find persons by name\n"
            + "  set-calorie-target — Set a client's calorie target\n"
            + "  log-calorie-intake — Log a client's calorie intake\n"
            + "  set-focus     — Set a client's workout focus\n"
            + "  remark        — Add a remark to a client\n"
            + "  clear         — Clear all entries\n"
            + "  help          — Show this help message\n"
            + "  exit          — Exit the application";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
