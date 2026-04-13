package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Client;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;
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

            // Defensive: ensure the invariant even if storage-side reconciliation changes.
            removeClientsWithMissingTrainers(model);

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
            if (!isTrainerNode(personNode)) {
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
            if (!isClientNode(personNode)) {
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

    private static boolean isTrainerNode(JsonNode personNode) {
        // Align with JsonAdaptedPerson's trainer/client inference for legacy records.
        JsonNode typeNode = personNode.get("type");
        String type = typeNode == null ? null : typeNode.asText(null);
        if ("trainer".equals(type)) {
            return true;
        }
        return type == null && personNode.hasNonNull("email");
    }

    private static boolean isClientNode(JsonNode personNode) {
        // Align with JsonAdaptedPerson's trainer/client inference for legacy records.
        JsonNode typeNode = personNode.get("type");
        String type = typeNode == null ? null : typeNode.asText(null);
        if ("client".equals(type)) {
            return true;
        }
        return type == null && personNode.hasNonNull("trainerPhone");
    }

    /**
     * Removes any {@link Client} whose {@code trainerPhone} does not match any loaded {@link Trainer}.
     *
     * <p>Done at command level so import behavior remains consistent even if storage-side reconciliation changes.
     */
    private static int removeClientsWithMissingTrainers(Model model) {
        Set<Phone> trainerPhones = model.getAddressBook().getPersonList().stream()
                .filter(Trainer.class::isInstance)
                .map(Trainer.class::cast)
                .map(Trainer::getPhone)
                .collect(Collectors.toSet());

        int removedClients = 0;
        for (Person person : new ArrayList<>(model.getAddressBook().getPersonList())) {
            if (!(person instanceof Client)) {
                continue;
            }
            Client client = (Client) person;
            if (!trainerPhones.contains(client.getTrainerPhone())) {
                model.deletePerson(client);
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
