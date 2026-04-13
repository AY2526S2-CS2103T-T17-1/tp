package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.storage.JsonAddressBookStorage;

//@@author TheSputnikSpacecraft
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
            Optional<JsonNode> rootOptional = JsonUtil.readJsonFile(filePath, JsonNode.class);
            if (rootOptional.isEmpty()) {
                throw new CommandException(String.format(MESSAGE_FILE_NOT_FOUND, filePath.toString()));
            }

            int removedClients = countClientsWithMissingTrainers(rootOptional.get());

            Optional<ReadOnlyAddressBook> addressBookOptional = storage.readAddressBook();
            if (addressBookOptional.isEmpty()) {
                throw new CommandException(String.format(MESSAGE_FILE_NOT_FOUND, filePath.toString()));
            }

            model.setAddressBook(addressBookOptional.get());

            String message = String.format(MESSAGE_SUCCESS, filePath.toString());
            if (removedClients > 0) {
                message += String.format(" (removed %d client(s) with missing trainers)", removedClients);
            }

            return new CommandResult(message);
        } catch (DataLoadingException e) {
            String errorMessage = e.getMessage();
            if (e.getCause() instanceof java.nio.file.AccessDeniedException) {
                errorMessage = "Permission denied to read from file.";
            }
            throw new CommandException(String.format(MESSAGE_IMPORT_FAILURE, filePath.toString(), errorMessage));
        }
    }

    private static int countClientsWithMissingTrainers(JsonNode root) {
        if (root == null) {
            return 0;
        }

        JsonNode personsNode = root.get("persons");
        if (personsNode == null || !personsNode.isArray()) {
            return 0;
        }

        Set<String> trainerPhones = new java.util.HashSet<>();
        for (JsonNode personNode : personsNode) {
            if (personNode == null) {
                continue;
            }
            String type = personNode.path("type").asText("");
            if (!"trainer".equals(type)) {
                continue;
            }
            String trainerPhone = personNode.path("phone").asText("");
            if (!trainerPhone.isBlank()) {
                trainerPhones.add(trainerPhone);
            }
        }

        int removedClients = 0;
        for (JsonNode personNode : personsNode) {
            if (personNode == null) {
                continue;
            }
            String type = personNode.path("type").asText("");
            if (!"client".equals(type)) {
                continue;
            }
            String trainerPhone = personNode.path("trainerPhone").asText("");
            if (trainerPhone.isBlank()) {
                continue;
            }
            if (!trainerPhones.contains(trainerPhone)) {
                removedClients++;
            }
        }

        return removedClients;
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
