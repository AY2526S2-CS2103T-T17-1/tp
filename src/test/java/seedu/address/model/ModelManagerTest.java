package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;
import seedu.address.testutil.AddressBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }

    @Test
    public void setSelectedTrainer_filtersClientListAndTracksSelection() {
        AddressBook ab = new AddressBook();
        Trainer trainerA = new Trainer(new Name("TrainerA"), new Phone("90000001"),
                new Email("a@example.com"), Set.of());
        Trainer trainerB = new Trainer(new Name("TrainerB"), new Phone("90000002"),
                new Email("b@example.com"), Set.of());
        Client clientA1 = new Client(new Name("ClientA1"), new Phone("80000001"),
                trainerA.getPhone(), trainerA.getName(), new HashSet<>());
        Client clientB1 = new Client(new Name("ClientB1"), new Phone("80000002"),
                trainerB.getPhone(), trainerB.getName(), new HashSet<>());
        ab.addPerson(trainerA);
        ab.addPerson(trainerB);
        ab.addPerson(clientA1);
        ab.addPerson(clientB1);

        ModelManager model = new ModelManager(ab, new UserPrefs());

        model.setSelectedTrainer(trainerA);
        assertTrue(model.getSelectedTrainer().isPresent());
        assertEquals(trainerA.getPhone(), model.getSelectedTrainer().get().getPhone());
        assertEquals(List.of(clientA1), model.getFilteredClientList());

        model.clearSelectedTrainer();
        assertTrue(model.getSelectedTrainer().isEmpty());
        assertEquals(2, model.getFilteredClientList().size());
    }

    @Test
    public void setSelectedTrainer_trainerNotInAddressBook_selectionClears() {
        AddressBook ab = new AddressBook();
        Trainer existingTrainer = new Trainer(new Name("Existing"), new Phone("90000001"),
                new Email("e@example.com"), Set.of());
        ab.addPerson(existingTrainer);

        ModelManager model = new ModelManager(ab, new UserPrefs());
        Trainer notInBook = new Trainer(new Name("NotInBook"), new Phone("99999999"),
                new Email("n@example.com"), Set.of());

        model.setSelectedTrainer(notInBook);
        assertTrue(model.getSelectedTrainer().isEmpty());
    }

    @Test
    public void deletePerson_deletingSelectedTrainer_clearsSelection() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("Trainer"), new Phone("90000001"),
                new Email("t@example.com"), Set.of());
        Client client = new Client(new Name("Client"), new Phone("80000001"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(client);

        ModelManager model = new ModelManager(ab, new UserPrefs());
        model.setSelectedTrainer(trainer);
        assertTrue(model.getSelectedTrainer().isPresent());

        model.deletePerson(trainer);
        assertTrue(model.getSelectedTrainer().isEmpty());
        assertEquals(1, model.getFilteredClientList().size());
    }

    @Test
    public void setPerson_editSelectedTrainer_updatesSelectionAndRefreshesClients() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("Trainer"), new Phone("90000001"),
                new Email("t@example.com"), Set.of());
        Client client = new Client(new Name("Client"), new Phone("80000001"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(client);

        ModelManager model = new ModelManager(ab, new UserPrefs());
        model.setSelectedTrainer(trainer);

        Trainer editedTrainer = new Trainer(new Name("Trainer Edited"), new Phone("90000009"),
                new Email("t@example.com"), Set.of());
        model.setPerson(trainer, editedTrainer);

        assertEquals(Optional.of(editedTrainer), model.getSelectedTrainer());
        assertEquals(0, model.getFilteredClientList().size());
    }

    @Test
    public void setPerson_editSelectedTrainerToClient_clearsSelectionAndShowsAllClients() {
        AddressBook ab = new AddressBook();
        Trainer trainer = new Trainer(new Name("Trainer"), new Phone("90000001"),
                new Email("t@example.com"), Set.of());
        Client client = new Client(new Name("Client"), new Phone("80000001"),
                trainer.getPhone(), trainer.getName(), new HashSet<>());
        ab.addPerson(trainer);
        ab.addPerson(client);

        ModelManager model = new ModelManager(ab, new UserPrefs());
        model.setSelectedTrainer(trainer);

        Client editedToClient = new Client(new Name("Now Client"), trainer.getPhone(),
                new Phone("91234567"), new Name("Other Trainer"), new HashSet<>());
        model.setPerson(trainer, editedToClient);

        assertTrue(model.getSelectedTrainer().isEmpty());
        assertEquals(2, model.getFilteredClientList().size());
    }
}
