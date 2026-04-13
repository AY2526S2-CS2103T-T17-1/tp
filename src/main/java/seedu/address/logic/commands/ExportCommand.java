package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.storage.JsonAddressBookStorage;

/**
 * Exports GymOps data to a specified JSON file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports GymOps data to a specified JSON file.\n"
            + "Parameters: FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " data/my_export.json";

    public static final String MESSAGE_SUCCESS = "GymOps data exported successfully to: %1$s";
    public static final String MESSAGE_EXPORT_FAILURE = "Failed to export GymOps data to: %1$s. Error: %2$s";

    private final Path filePath;

    /**
     * Creates an ExportCommand to export the AddressBook to the specified {@code Path}.
     */
    public ExportCommand(Path filePath) {
        requireNonNull(filePath);
        this.filePath = filePath;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        try {
            storage.saveAddressBook(model.getAddressBook());
            return new CommandResult(String.format(MESSAGE_SUCCESS, filePath.toString()));
        } catch (IOException e) {
            String errorMessage = e.getMessage();
            if (e instanceof java.nio.file.AccessDeniedException) {
                errorMessage = "Permission denied to write to file.";
            }
            throw new CommandException(String.format(MESSAGE_EXPORT_FAILURE, filePath.toString(), errorMessage));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ExportCommand)) {
            return false;
        }

        ExportCommand otherExportCommand = (ExportCommand) other;
        return filePath.equals(otherExportCommand.filePath);
    }
}
