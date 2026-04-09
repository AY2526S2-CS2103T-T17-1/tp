package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Client;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");
    private static final Path CLIENT_WITH_MISSING_TRAINER_FILE =
            TEST_DATA_FOLDER.resolve("clientWithMissingTrainer.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalPersonsAddressBook = TypicalPersons.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalPersonsAddressBook);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_clientWithMissingTrainer_rogueClientRemoved() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(CLIENT_WITH_MISSING_TRAINER_FILE,
                JsonSerializableAddressBook.class).get();

        AddressBook addressBookFromFile = dataFromFile.toModelType();
        // File contains 1 trainer, 1 valid client, and 1 rogue client.
        assertEquals(2, addressBookFromFile.getPersonList().size());

        boolean containsRogueClient = addressBookFromFile.getPersonList().stream()
                .map(Person::getPhone)
                .anyMatch(phone -> phone.getValue().equals("90000000"));
        assertEquals(false, containsRogueClient);
    }

    @Test
    public void toModelType_clientWithMismatchedTrainerName_trainerNameReconciled() throws Exception {
        JsonAdaptedPerson trainer = new JsonAdaptedPerson("trainer", "Real Trainer", "92222222",
                "real@trainer.com", null, null, 0, 0, null, null, null, List.of());
        JsonAdaptedPerson client = new JsonAdaptedPerson("client", "Client", "90000002", null,
                "92222222", "Wrong Name", 0, 0, null, null, null, List.of());

        JsonSerializableAddressBook serializable = new JsonSerializableAddressBook(List.of(trainer, client));
        AddressBook addressBook = serializable.toModelType();

        Client loadedClient = addressBook.getPersonList().stream()
                .filter(Client.class::isInstance)
                .map(Client.class::cast)
                .findFirst()
                .orElseThrow();
        assertEquals("Real Trainer", loadedClient.getTrainerName().getFullName());
    }

}
