package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
    private final SortedList<Person> sortedTrainers;
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

        logger.fine("Initializing with address book: " + addressBook
            + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);

        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredTrainers = new FilteredList<>(this.addressBook.getPersonList());
        sortedTrainers = new SortedList<>(filteredTrainers);
        filteredClients = new FilteredList<>(this.addressBook.getPersonList());

        refreshAllPredicates();
    }

    /**
     * Initializes an empty {@code ModelManager}.
     */
    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs =================================================================
    /** {@inheritDoc} */
    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    /** {@inheritDoc} */
    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    /** {@inheritDoc} */
    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    /** {@inheritDoc} */
    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    /** {@inheritDoc} */
    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    /** {@inheritDoc} */
    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================
    /** {@inheritDoc} */
    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
        clearSelectedTrainer();
        refreshAllPredicates();
    }

    /** {@inheritDoc} */
    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasClientWithTrainer(Trainer trainer) {
        requireNonNull(trainer);
        return addressBook.hasClientWithTrainer(trainer);
    }

    /** {@inheritDoc} */
    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
        if (target instanceof Trainer && selectedTrainerPhone.isPresent()
                && ((Trainer) target).getPhone().equals(selectedTrainerPhone.get())) {
            clearSelectedTrainer();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    /** {@inheritDoc} */
    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        if (target instanceof Trainer && editedPerson instanceof Trainer) {
            Trainer originalTrainer = (Trainer) target;
            Trainer editedTrainer = (Trainer) editedPerson;
            boolean trainerPhoneChanged = !originalTrainer.getPhone().equals(editedTrainer.getPhone());
            boolean trainerNameChanged = !originalTrainer.getName().equals(editedTrainer.getName());

            addressBook.setPerson(target, editedPerson);
            if (trainerPhoneChanged || trainerNameChanged) {
                updateClientsAssignedToTrainer(originalTrainer.getPhone(), editedTrainer);
            }
        } else {
            addressBook.setPerson(target, editedPerson);
        }

        updateSelectedTrainerAfterEdit(target, editedPerson);
    }

    private void updateClientsAssignedToTrainer(Phone originalTrainerPhone, Trainer editedTrainer) {
        Phone editedTrainerPhone = editedTrainer.getPhone();
        addressBook.getPersonList().stream()
                .filter(person -> person instanceof Client)
                .map(person -> (Client) person)
                .filter(client -> client.getTrainerPhone().equals(originalTrainerPhone))
                .toList()
                .forEach(client -> addressBook.setPerson(
                        client,
                        client.withTrainer(editedTrainerPhone, editedTrainer.getName())));
    }

    private void updateSelectedTrainerAfterEdit(Person target, Person editedPerson) {
        if (!(target instanceof Trainer) || selectedTrainerPhone.isEmpty()) {
            return;
        }

        Phone selectedPhone = selectedTrainerPhone.get();
        if (!((Trainer) target).getPhone().equals(selectedPhone)) {
            return;
        }

        if (editedPerson instanceof Trainer) {
            selectedTrainerPhone = Optional.of(((Trainer) editedPerson).getPhone());
        } else {
            clearSelectedTrainer();
        }

        refreshClientPredicate();
    }

    //=========== Filtered Person List Accessors ============================================
    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the
     * internal list of {@code addressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    /** {@inheritDoc} */
    @Override
    public ObservableList<Person> getFilteredTrainerList() {
        return sortedTrainers;
    }

    /** {@inheritDoc} */
    @Override
    public ObservableList<Person> getFilteredClientList() {
        return filteredClients;
    }

    /** {@inheritDoc} */
    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);

        personListPredicate = predicate;
        trainerListPredicate = predicate;
        clientListPredicate = predicate;

        clearSelectedTrainer();
        refreshAllPredicates();
    }

    /** {@inheritDoc} */
    @Override
    public void updateFilteredTrainerList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        trainerListPredicate = predicate;
        refreshTrainerPredicate();
        sortedTrainers.setComparator(null);
    }

    @Override
    public void updateSortedTrainerList(java.util.Comparator<Person> comparator) {
        sortedTrainers.setComparator(comparator);
    }

    /** {@inheritDoc} */
    @Override
    public void updateFilteredClientList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        clientListPredicate = predicate;
        refreshClientPredicate();
    }

    /** {@inheritDoc} */
    @Override
    public void setSelectedTrainer(Trainer trainer) {
        requireNonNull(trainer);
        selectedTrainerPhone = Optional.of(trainer.getPhone());
        refreshClientPredicate();
    }

    /** {@inheritDoc} */
    @Override
    public void clearSelectedTrainer() {
        selectedTrainerPhone = Optional.empty();
        refreshClientPredicate();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isTrainerListFiltered() {
        return trainerListPredicate != PREDICATE_SHOW_ALL_PERSONS;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isClientListFiltered() {
        return clientListPredicate != PREDICATE_SHOW_ALL_PERSONS;
    }

    /** {@inheritDoc} */
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
        filteredTrainers.setPredicate(
                person -> person instanceof Trainer && trainerListPredicate.test(person));
    }

    private void refreshClientPredicate() {
        Predicate<Person> selectionPredicate = person -> true;
        if (selectedTrainerPhone.isPresent()) {
            Phone trainerPhone = selectedTrainerPhone.get();
            selectionPredicate = person -> person instanceof Client
                    && ((Client) person).getTrainerPhone().equals(trainerPhone);
        }

        Predicate<Person> typeAndUserPredicate =
            person -> person instanceof Client && clientListPredicate.test(person);
        filteredClients.setPredicate(typeAndUserPredicate.and(selectionPredicate));

        // If the selected trainer no longer exists, clear it to keep UI state consistent.
        if (selectedTrainerPhone.isPresent() && getSelectedTrainer().isEmpty()) {
            selectedTrainerPhone = Optional.empty();
            filteredClients.setPredicate(typeAndUserPredicate);
        }
    }

    /** {@inheritDoc} */
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
                && sortedTrainers.equals(otherModelManager.sortedTrainers)
                && filteredClients.equals(otherModelManager.filteredClients)
                && personListPredicate.equals(otherModelManager.personListPredicate)
                && trainerListPredicate.equals(otherModelManager.trainerListPredicate)
                && clientListPredicate.equals(otherModelManager.clientListPredicate)
                && selectedTrainerPhone.equals(otherModelManager.selectedTrainerPhone);
    }

}
