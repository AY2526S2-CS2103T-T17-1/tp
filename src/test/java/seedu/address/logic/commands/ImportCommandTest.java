package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;
import seedu.address.storage.JsonAddressBookStorage;

public class ImportCommandTest {

    @TempDir
    public Path testFolder;

    @Test
    public void execute_importSuccess() throws Exception {
        // First, create a valid JSON file to import
        Path importPath = testFolder.resolve("import.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(importPath);
        storage.saveAddressBook(getTypicalAddressBook());

        // Setup models
        Model model = new ModelManager(); // empty model
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        ImportCommand importCommand = new ImportCommand(importPath);

        String expectedMessage = String.format(ImportCommand.MESSAGE_SUCCESS, importPath.toString());

        assertCommandSuccess(importCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_importSuccess_removedCountReported() throws Exception {
        Path importPath = testFolder.resolve("importWithRogueClient.json");

        AddressBook addressBook = new AddressBook();
        Trainer trainer = new Trainer(new Name("Real Trainer"), new Phone("92222222"),
                new Email("real@trainer.com"), java.util.Set.of());
        Client validClient = new Client(new Name("Valid Client"), new Phone("90000001"),
                trainer.getPhone(), trainer.getName(), java.util.Set.of());
        Client rogueClient = new Client(new Name("Rogue Client"), new Phone("90000000"),
                new Phone("91111111"), new Name("Nonexistent Trainer"), java.util.Set.of());

        addressBook.addPerson(trainer);
        addressBook.addPerson(validClient);
        addressBook.addPerson(rogueClient);

        JsonAddressBookStorage storage = new JsonAddressBookStorage(importPath);
        storage.saveAddressBook(addressBook);

        Model model = new ModelManager();

        AddressBook expectedAddressBook = new AddressBook();
        expectedAddressBook.addPerson(trainer);
        expectedAddressBook.addPerson(validClient);
        Model expectedModel = new ModelManager(expectedAddressBook, new UserPrefs());

        ImportCommand importCommand = new ImportCommand(importPath);
        String expectedMessage = String.format(ImportCommand.MESSAGE_SUCCESS, importPath.toString())
                + " (removed 1 client(s) with missing trainers)";

        assertCommandSuccess(importCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_importSuccess_trainerNameReconciled() throws Exception {
        Path importPath = testFolder.resolve("importWithMismatchedTrainerName.json");

        AddressBook addressBook = new AddressBook();
        Trainer trainer = new Trainer(new Name("Real Trainer"), new Phone("92222222"),
                new Email("real@trainer.com"), java.util.Set.of());
        Client clientWithWrongTrainerName = new Client(new Name("Client"), new Phone("90000002"),
                trainer.getPhone(), new Name("Wrong Name"), java.util.Set.of());
        addressBook.addPerson(trainer);
        addressBook.addPerson(clientWithWrongTrainerName);

        JsonAddressBookStorage storage = new JsonAddressBookStorage(importPath);
        storage.saveAddressBook(addressBook);

        Model model = new ModelManager();

        AddressBook expectedAddressBook = new AddressBook();
        expectedAddressBook.addPerson(trainer);
        expectedAddressBook.addPerson(new Client(new Name("Client"), new Phone("90000002"),
                trainer.getPhone(), trainer.getName(), java.util.Set.of()));
        Model expectedModel = new ModelManager(expectedAddressBook, new UserPrefs());

        ImportCommand importCommand = new ImportCommand(importPath);
        String expectedMessage = String.format(ImportCommand.MESSAGE_SUCCESS, importPath.toString());

        assertCommandSuccess(importCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_fileNotFound_throwsCommandException() {
        Path importPath = testFolder.resolve("nonExistent.json");
        ImportCommand importCommand = new ImportCommand(importPath);
        Model model = new ModelManager();

        String expectedMessage = String.format(ImportCommand.MESSAGE_FILE_NOT_FOUND, importPath.toString());

        assertCommandFailure(importCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidFormat_throwsCommandException() throws Exception {
        Path importPath = testFolder.resolve("invalid.json");
        java.nio.file.Files.writeString(importPath, "invalid json content");

        ImportCommand importCommand = new ImportCommand(importPath);
        Model model = new ModelManager();

        seedu.address.testutil.Assert.assertThrows(CommandException.class, () -> importCommand.execute(model));
    }

    @Test
    public void equals() {
        Path path1 = Path.of("path1.json");
        Path path2 = Path.of("path2.json");
        ImportCommand importCommand1 = new ImportCommand(path1);
        ImportCommand importCommand2 = new ImportCommand(path2);

        // same object -> returns true
        assertTrue(importCommand1.equals(importCommand1));

        // same values -> returns true
        ImportCommand importCommand1Copy = new ImportCommand(path1);
        assertTrue(importCommand1.equals(importCommand1Copy));

        // different types -> returns false
        assertFalse(importCommand1.equals(1));

        // null -> returns false
        assertFalse(importCommand1.equals(null));

        // different command -> returns false
        assertFalse(importCommand1.equals(importCommand2));
    }

}
