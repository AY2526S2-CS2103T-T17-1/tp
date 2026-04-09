package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Edits the details of an existing trainer in GymOps.
 */
public class EditTrainerCommand extends Command {

    public static final String COMMAND_WORD = "edit-trainer";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the trainer identified "
            + "by the index number used in the displayed trainer list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "John Tan "
            + PREFIX_EMAIL + "johntan@example.com";

    public static final String MESSAGE_EDIT_TRAINER_SUCCESS =
            "Edited Trainer: %1$s";
    public static final String MESSAGE_NOT_EDITED =
            "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TRAINER =
            "This phone number is already in use, or this email is already used by another trainer.";
    public static final String MESSAGE_NOT_A_TRAINER =
            "The person at the specified index is not a Trainer.";

    private final Index index;
    private final EditTrainerDescriptor editTrainerDescriptor;

    /**
     * Creates an EditTrainerCommand to edit the trainer at {@code index}.
     *
     * @param index of the trainer in the filtered trainer list to edit.
     * @param editTrainerDescriptor details to edit the trainer with.
     */
    public EditTrainerCommand(Index index,
                              EditTrainerDescriptor editTrainerDescriptor) {
        requireNonNull(index);
        requireNonNull(editTrainerDescriptor);

        this.index = index;
        this.editTrainerDescriptor =
                new EditTrainerDescriptor(editTrainerDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredTrainerList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        if (!(personToEdit instanceof Trainer)) {
            throw new CommandException(MESSAGE_NOT_A_TRAINER);
        }

        Trainer trainerToEdit = (Trainer) personToEdit;
        Trainer editedTrainer = createEditedTrainer(
                trainerToEdit, editTrainerDescriptor);

        try {
            model.setPerson(trainerToEdit, editedTrainer);
        } catch (DuplicatePersonException e) {
            throw new CommandException(MESSAGE_DUPLICATE_TRAINER);
        }
        return new CommandResult(
                String.format(MESSAGE_EDIT_TRAINER_SUCCESS,
                        Messages.format(editedTrainer)));
    }

    /**
     * Creates and returns a {@code Trainer} with the details of
     * {@code trainerToEdit} edited with {@code descriptor}.
     */
    private static Trainer createEditedTrainer(
            Trainer trainerToEdit,
            EditTrainerDescriptor descriptor) {

        Name updatedName = descriptor.getName()
                .orElse(trainerToEdit.getName());
        Phone updatedPhone = descriptor.getPhone()
                .orElse(trainerToEdit.getPhone());
        Email updatedEmail = descriptor.getEmail()
                .orElse(trainerToEdit.getEmail());

        return new Trainer(updatedName, updatedPhone, updatedEmail,
                trainerToEdit.getTags());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditTrainerCommand)) {
            return false;
        }

        EditTrainerCommand otherCommand = (EditTrainerCommand) other;
        return index.equals(otherCommand.index)
                && editTrainerDescriptor.equals(
                        otherCommand.editTrainerDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editTrainerDescriptor", editTrainerDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the trainer with. Each non-empty field
     * value will replace the corresponding field value of the trainer.
     */
    public static class EditTrainerDescriptor {
        private Name name;
        private Phone phone;
        private Email email;

        public EditTrainerDescriptor() {
        }

        /**
         * Copy constructor.
         */
        public EditTrainerDescriptor(EditTrainerDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return name != null || phone != null || email != null;
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditTrainerDescriptor)) {
                return false;
            }

            EditTrainerDescriptor otherDescriptor =
                    (EditTrainerDescriptor) other;
            return Objects.equals(name, otherDescriptor.name)
                    && Objects.equals(phone, otherDescriptor.phone)
                    && Objects.equals(email, otherDescriptor.email);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .toString();
        }
    }
}
