package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Trainer;
import seedu.address.model.person.Validity;
import seedu.address.model.person.WorkoutFocus;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    public static final String INVALID_TYPE_MESSAGE_FORMAT = "Person's type is invalid!";
    public static final String NEGATIVE_CALORIE_TARGET_MESSAGE = "Client's calorieTarget must be non-negative!";
    public static final String NEGATIVE_CALORIE_INTAKE_MESSAGE = "Client's calorieIntake must be non-negative!";

    private final String type; // "trainer" or "client"
    private final String name;
    private final String phone;
    private final String email;
    private final String trainerPhone;
    private final String trainerName;
    private final int calorieTarget;
    private final int calorieIntake;
    private final String workoutFocus;
    private final String remark;
    private final String validity;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("type") String type, @JsonProperty("name") String name,
            @JsonProperty("phone") String phone, @JsonProperty("email") String email,
            @JsonProperty("trainerPhone") String trainerPhone, @JsonProperty("trainerName") String trainerName,
            @JsonProperty("calorieTarget") int calorieTarget,
            @JsonProperty("calorieIntake") int calorieIntake,
            @JsonProperty("workoutFocus") String workoutFocus,
            @JsonProperty("remark") String remark,
            @JsonProperty("validity") String validity,
            @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.trainerPhone = trainerPhone;
        this.trainerName = trainerName;
        this.calorieTarget = calorieTarget;
        this.calorieIntake = calorieIntake;
        this.workoutFocus = workoutFocus;
        this.remark = remark;
        this.validity = validity;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().getFullName();
        phone = source.getPhone().getValue();
        if (source instanceof Trainer) {
            type = "trainer";
            email = ((Trainer) source).getEmail().getValue();
            trainerPhone = null;
            trainerName = null;
            calorieTarget = 0;
            calorieIntake = 0;
            workoutFocus = null;
            remark = null;
            validity = null;
        } else if (source instanceof Client) {
            Client client = (Client) source;
            type = "client";
            email = null;
            trainerPhone = client.getTrainerPhone().getValue();
            trainerName = client.getTrainerName().getFullName();
            calorieTarget = client.getCalorieTarget();
            calorieIntake = client.getCalorieIntake();
            workoutFocus = client.getWorkoutFocus()
                    .map(WorkoutFocus::getValue)
                    .orElse(null);
            remark = client.getRemark()
                    .map(Remark::getValue)
                    .orElse(null);
            validity = client.getValidity()
                    .map(Validity::toString)
                    .orElse(null);
        } else {
            type = "unknown";
            email = null;
            trainerPhone = null;
            trainerName = null;
            calorieTarget = 0;
            calorieIntake = 0;
            workoutFocus = null;
            remark = null;
            validity = null;
        }

        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's
     * {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated
     *     in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        Name modelName = toModelName();
        Phone modelPhone = toModelPhone();
        Set<Tag> modelTags = toModelTags();

        if (isTrainerType()) {
            return toModelTrainer(modelName, modelPhone, modelTags);
        }

        if (isClientType()) {
            return toModelClient(modelName, modelPhone, modelTags);
        }

        throw new IllegalValueException(INVALID_TYPE_MESSAGE_FORMAT);
    }

    private boolean isTrainerType() {
        // fallback for existing records without type
        return "trainer".equals(type) || type == null && email != null;
    }

    private boolean isClientType() {
        // fallback for existing records without type
        return "client".equals(type) || type == null && trainerPhone != null;
    }

    private Set<Tag> toModelTags() throws IllegalValueException {
        List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }
        return new HashSet<>(personTags);
    }

    private Name toModelName() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(name);
    }

    private Phone toModelPhone() throws IllegalValueException {
        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(phone);
    }

    private Trainer toModelTrainer(Name modelName, Phone modelPhone, Set<Tag> modelTags) throws IllegalValueException {
        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }

        Email modelEmail = new Email(email);
        return new Trainer(modelName, modelPhone, modelEmail, modelTags);
    }

    private Client toModelClient(Name modelName, Phone modelPhone, Set<Tag> modelTags) throws IllegalValueException {
        Phone modelTrainerPhone = toModelTrainerPhone();
        Name modelTrainerName = toModelTrainerName();
        Optional<WorkoutFocus> modelFocus = toModelWorkoutFocus();
        Optional<Remark> modelRemark = toModelRemark();
        Optional<Validity> modelValidity = toModelValidity();

        if (calorieTarget < 0) {
            throw new IllegalValueException(NEGATIVE_CALORIE_TARGET_MESSAGE);
        }
        if (calorieIntake < 0) {
            throw new IllegalValueException(NEGATIVE_CALORIE_INTAKE_MESSAGE);
        }

        return new Client(modelName, modelPhone, modelTrainerPhone, modelTrainerName, modelTags,
                calorieTarget, calorieIntake, modelFocus, modelRemark, modelValidity);
    }

    private Phone toModelTrainerPhone() throws IllegalValueException {
        if (trainerPhone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "trainerPhone"));
        }
        if (!Phone.isValidPhone(trainerPhone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trainerPhone);
    }

    private Name toModelTrainerName() throws IllegalValueException {
        if (trainerName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "trainerName"));
        }
        if (!Name.isValidName(trainerName)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trainerName);
    }

    private Optional<WorkoutFocus> toModelWorkoutFocus() throws IllegalValueException {
        if (workoutFocus == null) {
            return Optional.empty();
        }
        if (!WorkoutFocus.isValidWorkoutFocus(workoutFocus)) {
            throw new IllegalValueException(WorkoutFocus.MESSAGE_CONSTRAINTS);
        }
        return Optional.of(new WorkoutFocus(workoutFocus));
    }

    private Optional<Remark> toModelRemark() throws IllegalValueException {
        if (remark == null) {
            return Optional.empty();
        }
        if (!Remark.isValidRemark(remark)) {
            throw new IllegalValueException(Remark.MESSAGE_CONSTRAINTS);
        }
        return Optional.of(new Remark(remark));
    }

    private Optional<Validity> toModelValidity() throws IllegalValueException {
        if (validity == null) {
            return Optional.empty();
        }
        if (!Validity.isValidValidity(validity)) {
            throw new IllegalValueException(Validity.MESSAGE_CONSTRAINTS);
        }
        if (!Validity.isNotPastDate(validity)) {
            throw new IllegalValueException(Validity.MESSAGE_PAST_DATE);
        }
        return Optional.of(new Validity(validity));
    }

}
