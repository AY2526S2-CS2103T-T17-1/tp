package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Client in the address book.
 * A Client is associated with a Trainer identified by {@code trainerPhone} and {@code trainerName}.
 * Optionally tracks a calorie target and accumulated daily calorie intake.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Client extends Person {

    private final Phone trainerPhone;
    private final Name trainerName;
    private final int calorieTarget;
    private final int calorieIntake;
    private final Optional<WorkoutFocus> workoutFocus;
    private final Optional<Remark> remark;
    private final Optional<Validity> validity;

    /**
     * Constructs a {@code Client} with all fields including calorie tracking data.
     * Every field must be present and not null.
     *
     * @param name          The client's name.
     * @param phone         The client's phone number.
     * @param trainerPhone  The phone number of the client's assigned trainer.
     * @param trainerName   The name of the client's assigned trainer.
     * @param tags          The set of tags associated with the client.
     * @param calorieTarget The daily calorie target (0 means not set).
     * @param calorieIntake The accumulated daily calorie intake.
     */
    public Client(Name name, Phone phone, Phone trainerPhone, Name trainerName, Set<Tag> tags,
                  int calorieTarget, int calorieIntake) {
        this(name, phone, trainerPhone, trainerName, tags, calorieTarget, calorieIntake, Optional.empty(),
                Optional.empty(), Optional.empty());
    }

    /**
     * Constructs a {@code Client} with all fields including calorie tracking data and optional client-only fields.
     * Use this constructor for backwards-compatibility when validity is not present.
     */
    public Client(Name name, Phone phone, Phone trainerPhone, Name trainerName, Set<Tag> tags,
                  int calorieTarget, int calorieIntake,
                  Optional<WorkoutFocus> workoutFocus, Optional<Remark> remark) {
        this(name, phone, trainerPhone, trainerName, tags, calorieTarget,
                calorieIntake, workoutFocus, remark, Optional.empty());
    }

    /**
     * Constructs a {@code Client} with all fields including calorie tracking data,
     * optional client-only fields, and validity.
     */
    public Client(Name name, Phone phone, Phone trainerPhone, Name trainerName, Set<Tag> tags,
                  int calorieTarget, int calorieIntake,
                  Optional<WorkoutFocus> workoutFocus, Optional<Remark> remark, Optional<Validity> validity) {
        super(name, phone, tags);
        requireAllNonNull(trainerPhone, trainerName);
        this.trainerPhone = trainerPhone;
        this.trainerName = trainerName;
        this.calorieTarget = calorieTarget;
        this.calorieIntake = calorieIntake;
        this.workoutFocus = workoutFocus == null ? Optional.empty() : workoutFocus;
        this.remark = remark == null ? Optional.empty() : remark;
        this.validity = validity == null ? Optional.empty() : validity;
    }

    /**
     * Constructs a {@code Client} with default calorie values of zero.
     * Every field must be present and not null.
     *
     * @param name         The client's name.
     * @param phone        The client's phone number.
     * @param trainerPhone The phone number of the client's assigned trainer.
     * @param trainerName  The name of the client's assigned trainer.
     * @param tags         The set of tags associated with the client.
     */
    public Client(Name name, Phone phone, Phone trainerPhone, Name trainerName, Set<Tag> tags) {
        this(name, phone, trainerPhone, trainerName, tags, 0, 0);
    }

    private Client copyWith(
            Phone trainerPhone,
            Name trainerName,
            int calorieTarget,
            int calorieIntake,
            Optional<WorkoutFocus> workoutFocus,
            Optional<Remark> remark,
            Optional<Validity> validity) {
        return new Client(
                getName(),
                getPhone(),
                trainerPhone,
                trainerName,
                getTags(),
                calorieTarget,
                calorieIntake,
                workoutFocus,
                remark,
                validity);
    }

    /**
     * Returns a copy of this client with an updated assigned trainer.
     */
    public Client withTrainer(Phone trainerPhone, Name trainerName) {
        requireAllNonNull(trainerPhone, trainerName);
        return copyWith(
                trainerPhone,
                trainerName,
                calorieTarget,
                calorieIntake,
                workoutFocus,
                remark,
                validity);
    }

    /**
     * Returns a copy of this client with an updated calorie target.
     */
    public Client withCalorieTarget(int calorieTarget) {
        return copyWith(
                trainerPhone,
                trainerName,
                calorieTarget,
                calorieIntake,
                workoutFocus,
                remark,
                validity);
    }

    /**
     * Returns a copy of this client with an updated calorie intake.
     */
    public Client withCalorieIntake(int calorieIntake) {
        return copyWith(
                trainerPhone,
                trainerName,
                calorieTarget,
                calorieIntake,
                workoutFocus,
                remark,
                validity);
    }

    /**
     * Returns a copy of this client with an updated workout focus.
     */
    public Client withWorkoutFocus(WorkoutFocus workoutFocus) {
        requireAllNonNull(workoutFocus);
        return copyWith(
                trainerPhone,
                trainerName,
                calorieTarget,
                calorieIntake,
                Optional.of(workoutFocus),
                remark,
                validity);
    }

    /**
     * Returns a copy of this client with an updated remark.
     */
    public Client withRemark(Remark remark) {
        requireAllNonNull(remark);
        return copyWith(
                trainerPhone,
                trainerName,
                calorieTarget,
                calorieIntake,
                workoutFocus,
                Optional.of(remark),
                validity);
    }

    /**
     * Returns a copy of this client with an updated validity.
     */
    public Client withValidity(Validity validity) {
        requireAllNonNull(validity);
        return copyWith(
                trainerPhone,
                trainerName,
                calorieTarget,
                calorieIntake,
                workoutFocus,
                remark,
                Optional.of(validity));
    }

    /**
     * Returns the client's workout focus if one has been set.
     */
    public Optional<WorkoutFocus> getWorkoutFocus() {
        return workoutFocus;
    }

    /**
     * Returns the client's remark if one has been set.
     */
    public Optional<Remark> getRemark() {
        return remark;
    }

    /**
     * Returns the client's validity if one has been set.
     */
    public Optional<Validity> getValidity() {
        return validity;
    }

    /**
     * Returns the phone number of the client's assigned trainer.
     */
    public Phone getTrainerPhone() {
        return trainerPhone;
    }

    /**
     * Returns the name of the client's assigned trainer.
     */
    public Name getTrainerName() {
        return trainerName;
    }

    /**
     * Returns the client's daily calorie target. A value of 0 indicates no target has been set.
     */
    public int getCalorieTarget() {
        return calorieTarget;
    }

    /**
     * Returns the client's accumulated daily calorie intake.
     */
    public int getCalorieIntake() {
        return calorieIntake;
    }

    /**
     * Returns true if both persons are Clients and have the same phone.
     * A Client and a Trainer sharing a phone number are NOT considered duplicates.
     */
    @Override
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }
        if (!(otherPerson instanceof Client)) {
            return false;
        }
        return getPhone().equals(otherPerson.getPhone());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Client)) {
            return false;
        }

        Client otherClient = (Client) other;
        return name.equals(otherClient.name)
                && phone.equals(otherClient.phone)
                && trainerPhone.equals(otherClient.trainerPhone)
                && trainerName.equals(otherClient.trainerName)
                && tags.equals(otherClient.tags)
                && calorieTarget == otherClient.calorieTarget
            && calorieIntake == otherClient.calorieIntake
            && workoutFocus.equals(otherClient.workoutFocus)
            && remark.equals(otherClient.remark)
            && validity.equals(otherClient.validity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, trainerPhone, trainerName, tags, calorieTarget, calorieIntake,
                workoutFocus, remark, validity);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("trainerPhone", trainerPhone)
                .add("trainerName", trainerName)
                .add("tags", tags)
                .add("calorieTarget", calorieTarget)
                .add("calorieIntake", calorieIntake)
                .add("workoutFocus", workoutFocus)
                .add("remark", remark)
                .add("validity", validity)
                .toString();
    }
}

