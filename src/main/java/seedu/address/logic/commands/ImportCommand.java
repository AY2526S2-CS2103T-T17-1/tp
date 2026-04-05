package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.storage.JsonAddressBookStorage;

/**
 * Imports GymOps data from a specified JSON file.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports GymOps data from a specified JSON file.\n"
            + "Parameters: FILE_PATH\n"
            + "Example: " + COMMAND_WORD + " data/my_import.json";

    public static final String MESSAGE_SUCCESS = "GymOps data imported successfully from: %1$s";
    public static final String MESSAGE_IMPORT_FAILURE = "Failed to import GymOps data from: %1$s. Error: %2$s";
    public static final String MESSAGE_FILE_NOT_FOUND = "Data file not found at: %1$s";

    private final Path filePath;

    /**
     * Creates an ImportCommand to import the AddressBook from the specified {@code Path}.
     */
    public ImportCommand(Path filePath) {
        requireNonNull(filePath);
        this.filePath = filePath;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        try {
            Optional<ReadOnlyAddressBook> addressBookOptional = storage.readAddressBook();
            if (addressBookOptional.isEmpty()) {
                throw new CommandException(String.format(MESSAGE_FILE_NOT_FOUND, filePath.toString()));
            }
            model.setAddressBook(addressBookOptional.get());
            return new CommandResult(String.format(MESSAGE_SUCCESS, filePath.toString()));
        } catch (DataLoadingException e) {
            String errorMessage = e.getMessage();
            if (e.getCause() instanceof java.nio.file.AccessDeniedException) {
                errorMessage = "Permission denied to read from file.";
            }
            throw new CommandException(String.format(MESSAGE_IMPORT_FAILURE, filePath.toString(), errorMessage));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ImportCommand)) {
            return false;
        }

        ImportCommand otherImportCommand = (ImportCommand) other;
        return filePath.equals(otherImportCommand.filePath);
    }
}
