package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CALORIE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FOCUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TRAINER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VALIDITY;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Client;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Trainer;
import seedu.address.model.person.Validity;
import seedu.address.model.person.WorkoutFocus;

/**
 * Edits the details of an existing client in GymOps.
 */
public class EditClientCommand extends Command {

    public static final String COMMAND_WORD = "edit-client";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the client identified "
            + "by the index number used in the displayed client list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_TRAINER + "TRAINER_INDEX] "
            + "[" + PREFIX_CALORIE + "CALORIE_TARGET] "
            + "[" + PREFIX_FOCUS + "FOCUS] "
            + "[" + PREFIX_REMARK + "REMARK] "
            + "[" + PREFIX_VALIDITY + "VALIDITY]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "Alice Tan "
            + PREFIX_PHONE + "91234567";

    public static final String MESSAGE_EDIT_CLIENT_SUCCESS = "Edited Client: %1$s";
    public static final String MESSAGE_NOT_EDITED =
            "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_CLIENT =
            "This client already exists in GymOps.";
    public static final String MESSAGE_NOT_A_CLIENT =
            "The person at the specified index is not a Client.";
    public static final String MESSAGE_INVALID_TRAINER =
            "The provided trainer index does not correspond to a Trainer.";

    private final Index index;
    private final EditClientDescriptor editClientDescriptor;

    /**
     * Creates an EditClientCommand to edit the client at {@code index}.
     *
     * @param index of the client in the filtered client list to edit.
     * @param editClientDescriptor details to edit the client with.
     */
    public EditClientCommand(Index index,
                             EditClientDescriptor editClientDescriptor) {
        requireNonNull(index);
        requireNonNull(editClientDescriptor);

        this.index = index;
        this.editClientDescriptor = new EditClientDescriptor(editClientDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredClientList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        if (!(personToEdit instanceof Client)) {
            throw new CommandException(MESSAGE_NOT_A_CLIENT);
        }

        Client clientToEdit = (Client) personToEdit;
        Client editedClient = createEditedClient(
                clientToEdit, editClientDescriptor, model);

        if (!clientToEdit.isSamePerson(editedClient)
                && model.hasPerson(editedClient)) {
            throw new CommandException(MESSAGE_DUPLICATE_CLIENT);
        }

        model.setPerson(clientToEdit, editedClient);
        return new CommandResult(
                String.format(MESSAGE_EDIT_CLIENT_SUCCESS,
                        Messages.format(editedClient)));
    }

    /**
     * Creates and returns a {@code Client} with the details of
     * {@code clientToEdit} edited with {@code descriptor}.
     */
    private static Client createEditedClient(
            Client clientToEdit,
            EditClientDescriptor descriptor,
            Model model) throws CommandException {

        Name updatedName = descriptor.getName()
                .orElse(clientToEdit.getName());
        Phone updatedPhone = descriptor.getPhone()
                .orElse(clientToEdit.getPhone());

        // Resolve trainer
        Phone trainerPhone = clientToEdit.getTrainerPhone();
        Name trainerName = clientToEdit.getTrainerName();

        if (descriptor.getTrainerIndex().isPresent()) {
            Index trainerIndex = descriptor.getTrainerIndex().get();
            List<Person> trainerList = model.getFilteredTrainerList();

            if (trainerIndex.getZeroBased() >= trainerList.size()) {
                throw new CommandException(
                        Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person trainerPerson =
                    trainerList.get(trainerIndex.getZeroBased());

            if (!(trainerPerson instanceof Trainer)) {
                throw new CommandException(MESSAGE_INVALID_TRAINER);
            }

            Trainer trainer = (Trainer) trainerPerson;
            trainerPhone = trainer.getPhone();
            trainerName = trainer.getName();
        }

        int updatedCalorieTarget = descriptor.getCalorieTarget()
                .orElse(clientToEdit.getCalorieTarget());

        Optional<WorkoutFocus> updatedFocus;
        if (descriptor.isClearWorkoutFocus()) {
            updatedFocus = Optional.empty();
        } else {
            updatedFocus = descriptor.getWorkoutFocus().isPresent()
                    ? Optional.of(descriptor.getWorkoutFocus().get())
                    : clientToEdit.getWorkoutFocus();
        }

        Optional<Remark> updatedRemark;
        if (descriptor.isClearRemark()) {
            updatedRemark = Optional.empty();
        } else {
            updatedRemark = descriptor.getRemark().isPresent()
                    ? Optional.of(descriptor.getRemark().get())
                    : clientToEdit.getRemark();
        }

        Optional<Validity> updatedValidity;
        if (descriptor.isClearValidity()) {
            updatedValidity = Optional.empty();
        } else {
            updatedValidity = descriptor.getValidity().isPresent()
                    ? Optional.of(descriptor.getValidity().get())
                    : clientToEdit.getValidity();
        }

        return new Client(updatedName, updatedPhone, trainerPhone,
                trainerName, clientToEdit.getTags(),
                updatedCalorieTarget, clientToEdit.getCalorieIntake(),
                updatedFocus, updatedRemark, updatedValidity);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditClientCommand)) {
            return false;
        }

        EditClientCommand otherCommand = (EditClientCommand) other;
        return index.equals(otherCommand.index)
                && editClientDescriptor.equals(
                        otherCommand.editClientDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editClientDescriptor", editClientDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the client with. Each non-empty field
     * value will replace the corresponding field value of the client.
     */
    public static class EditClientDescriptor {
        private Name name;
        private Phone phone;
        private Index trainerIndex;
        private Integer calorieTarget;
        private WorkoutFocus workoutFocus;
        private Remark remark;
        private Validity validity;
        private boolean isClearWorkoutFocus = false;
        private boolean isClearRemark = false;
        private boolean isClearValidity = false;

        public EditClientDescriptor() {
        }

        /**
         * Copy constructor.
         */
        public EditClientDescriptor(EditClientDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setTrainerIndex(toCopy.trainerIndex);
            setCalorieTarget(toCopy.calorieTarget);
            setWorkoutFocus(toCopy.workoutFocus);
            setRemark(toCopy.remark);
            setValidity(toCopy.validity);
            setClearWorkoutFocus(toCopy.isClearWorkoutFocus);
            setClearRemark(toCopy.isClearRemark);
            setClearValidity(toCopy.isClearValidity);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return name != null || phone != null || trainerIndex != null
                    || calorieTarget != null || workoutFocus != null || isClearWorkoutFocus
                    || remark != null || isClearRemark
                    || validity != null || isClearValidity;
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

        public void setTrainerIndex(Index trainerIndex) {
            this.trainerIndex = trainerIndex;
        }

        public Optional<Index> getTrainerIndex() {
            return Optional.ofNullable(trainerIndex);
        }

        public void setCalorieTarget(Integer calorieTarget) {
            this.calorieTarget = calorieTarget;
        }

        public Optional<Integer> getCalorieTarget() {
            return Optional.ofNullable(calorieTarget);
        }

        public void setWorkoutFocus(WorkoutFocus workoutFocus) {
            this.workoutFocus = workoutFocus;
        }

        public Optional<WorkoutFocus> getWorkoutFocus() {
            return Optional.ofNullable(workoutFocus);
        }

        public void setRemark(Remark remark) {
            this.remark = remark;
        }

        public Optional<Remark> getRemark() {
            return Optional.ofNullable(remark);
        }

        public void setValidity(Validity validity) {
            this.validity = validity;
        }

        public Optional<Validity> getValidity() {
            return Optional.ofNullable(validity);
        }

        public void setClearWorkoutFocus(boolean isClearWorkoutFocus) {
            this.isClearWorkoutFocus = isClearWorkoutFocus;
        }

        public boolean isClearWorkoutFocus() {
            return isClearWorkoutFocus;
        }

        public void setClearRemark(boolean isClearRemark) {
            this.isClearRemark = isClearRemark;
        }

        public boolean isClearRemark() {
            return isClearRemark;
        }

        public void setClearValidity(boolean isClearValidity) {
            this.isClearValidity = isClearValidity;
        }

        public boolean isClearValidity() {
            return isClearValidity;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditClientDescriptor)) {
                return false;
            }

            EditClientDescriptor otherDescriptor =
                    (EditClientDescriptor) other;
            return Objects.equals(name, otherDescriptor.name)
                    && Objects.equals(phone, otherDescriptor.phone)
                    && Objects.equals(trainerIndex,
                            otherDescriptor.trainerIndex)
                    && Objects.equals(calorieTarget,
                            otherDescriptor.calorieTarget)
                    && Objects.equals(workoutFocus,
                            otherDescriptor.workoutFocus)
                    && Objects.equals(remark, otherDescriptor.remark)
                    && Objects.equals(validity, otherDescriptor.validity)
                    && isClearWorkoutFocus == otherDescriptor.isClearWorkoutFocus
                    && isClearRemark == otherDescriptor.isClearRemark
                    && isClearValidity == otherDescriptor.isClearValidity;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("trainerIndex", trainerIndex)
                    .add("calorieTarget", calorieTarget)
                    .add("workoutFocus", workoutFocus)
                    .add("remark", remark)
                    .add("validity", validity)
                    .toString();
        }
    }
}
