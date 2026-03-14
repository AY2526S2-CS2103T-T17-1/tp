package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Client;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;

    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Person> filteredTrainers;
    private final FilteredList<Person> filteredClients;

    private Predicate<Person> personListPredicate = PREDICATE_SHOW_ALL_PERSONS;
    private Predicate<Person> trainerListPredicate = PREDICATE_SHOW_ALL_PERSONS;
    private Predicate<Person> clientListPredicate = PREDICATE_SHOW_ALL_PERSONS;

    private Optional<Phone> selectedTrainerPhone = Optional.empty();

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);

        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredTrainers = new FilteredList<>(this.addressBook.getPersonList());
        filteredClients = new FilteredList<>(this.addressBook.getPersonList());

        refreshAllPredicates();
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
        clearSelectedTrainer();
        refreshAllPredicates();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public boolean hasClientWithTrainer(Trainer trainer) {
        requireNonNull(trainer);
        return addressBook.hasClientWithTrainer(trainer);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
        if (target instanceof Trainer && selectedTrainerPhone.isPresent()
                && ((Trainer) target).getPhone().equals(selectedTrainerPhone.get())) {
            clearSelectedTrainer();
        }
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);

        if (target instanceof Trainer && selectedTrainerPhone.isPresent()) {
            Phone selectedPhone = selectedTrainerPhone.get();
            if (((Trainer) target).getPhone().equals(selectedPhone)) {
                if (editedPerson instanceof Trainer) {
                    selectedTrainerPhone = Optional.of(((Trainer) editedPerson).getPhone());
                } else {
                    clearSelectedTrainer();
                }
                refreshClientPredicate();
            }
        }
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public ObservableList<Person> getFilteredTrainerList() {
        return filteredTrainers;
    }

    @Override
    public ObservableList<Person> getFilteredClientList() {
        return filteredClients;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);

        personListPredicate = predicate;
        trainerListPredicate = predicate;
        clientListPredicate = predicate;

        clearSelectedTrainer();
        refreshAllPredicates();
    }

    @Override
    public void updateFilteredTrainerList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        trainerListPredicate = predicate;
        refreshTrainerPredicate();
    }

    @Override
    public void updateFilteredClientList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        clientListPredicate = predicate;
        refreshClientPredicate();
    }

    @Override
    public void setSelectedTrainer(Trainer trainer) {
        requireNonNull(trainer);
        selectedTrainerPhone = Optional.of(trainer.getPhone());
        refreshClientPredicate();
    }

    @Override
    public void clearSelectedTrainer() {
        selectedTrainerPhone = Optional.empty();
        refreshClientPredicate();
    }

    @Override
    public Optional<Trainer> getSelectedTrainer() {
        if (selectedTrainerPhone.isEmpty()) {
            return Optional.empty();
        }

        Phone trainerPhone = selectedTrainerPhone.get();
        return addressBook.getPersonList().stream()
                .filter(person -> person instanceof Trainer)
                .map(person -> (Trainer) person)
                .filter(trainer -> trainer.getPhone().equals(trainerPhone))
                .findFirst();
    }

    private void refreshAllPredicates() {
        filteredPersons.setPredicate(personListPredicate);
        refreshTrainerPredicate();
        refreshClientPredicate();
    }

    private void refreshTrainerPredicate() {
        filteredTrainers.setPredicate(person -> person instanceof Trainer && trainerListPredicate.test(person));
    }

    private void refreshClientPredicate() {
        Predicate<Person> selectionPredicate = person -> true;
        if (selectedTrainerPhone.isPresent()) {
            Phone trainerPhone = selectedTrainerPhone.get();
            selectionPredicate = person -> person instanceof Client
                    && ((Client) person).getTrainerPhone().equals(trainerPhone);
        }

        Predicate<Person> typeAndUserPredicate = person -> person instanceof Client && clientListPredicate.test(person);
        filteredClients.setPredicate(typeAndUserPredicate.and(selectionPredicate));

        // If the selected trainer no longer exists, clear it to keep UI state consistent.
        if (selectedTrainerPhone.isPresent() && getSelectedTrainer().isEmpty()) {
            selectedTrainerPhone = Optional.empty();
            filteredClients.setPredicate(typeAndUserPredicate);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
            && filteredPersons.equals(otherModelManager.filteredPersons)
            && filteredTrainers.equals(otherModelManager.filteredTrainers)
            && filteredClients.equals(otherModelManager.filteredClients)
            && personListPredicate.equals(otherModelManager.personListPredicate)
            && trainerListPredicate.equals(otherModelManager.trainerListPredicate)
            && clientListPredicate.equals(otherModelManager.clientListPredicate)
            && selectedTrainerPhone.equals(otherModelManager.selectedTrainerPhone);
    }

}
