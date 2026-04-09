package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

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

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+65 1234a";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_WORKOUT_FOCUS = "Chest!";
    private static final String INVALID_REMARK = " ";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = ((Trainer) BENSON).getEmail().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    private static final String VALID_CLIENT_NAME = "Alice";
    private static final String VALID_CLIENT_PHONE = "81234567";
    private static final String VALID_TRAINER_PHONE = "91234567";
    private static final String VALID_TRAINER_NAME = "John";

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_validClientDetails_returnsClient() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_CLIENT_NAME, VALID_CLIENT_PHONE, null,
                VALID_TRAINER_PHONE, VALID_TRAINER_NAME, 2000, 500, "Chest", "Recovering", "2026-12-31", VALID_TAGS);

        Client expected = new Client(new Name(VALID_CLIENT_NAME), new Phone(VALID_CLIENT_PHONE),
                new Phone(VALID_TRAINER_PHONE), new Name(VALID_TRAINER_NAME),
                BENSON.getTags(), 2000, 500,
                Optional.of(new WorkoutFocus("Chest")),
                Optional.of(new Remark("Recovering")),
                Optional.of(new Validity("2026-12-31")));

        assertEquals(expected, person.toModelType());
    }

    @Test
    public void toModelType_negativeCalorieTarget_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_CLIENT_NAME, VALID_CLIENT_PHONE, null,
                VALID_TRAINER_PHONE, VALID_TRAINER_NAME, -1, 0, null, null, null, VALID_TAGS);
        assertThrows(IllegalValueException.class,
                JsonAdaptedPerson.NEGATIVE_CALORIE_TARGET_MESSAGE,
                person::toModelType);
    }

    @Test
    public void toModelType_negativeCalorieIntake_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_CLIENT_NAME, VALID_CLIENT_PHONE, null,
                VALID_TRAINER_PHONE, VALID_TRAINER_NAME, 0, -10, null, null, null, VALID_TAGS);
        assertThrows(IllegalValueException.class,
                JsonAdaptedPerson.NEGATIVE_CALORIE_INTAKE_MESSAGE,
                person::toModelType);
    }

    @Test
    public void constructor_fromClientRoundTrip_returnsClient() throws Exception {
        Trainer trainer = new Trainer(new Name("Trainer"), new Phone("90000001"),
                new Email("trainer@example.com"), Set.of());

        Client client = new Client(new Name("Client"), new Phone("80000001"),
                trainer.getPhone(), trainer.getName(), BENSON.getTags(),
                2000, 500,
            Optional.of(new WorkoutFocus("Chest")),
            Optional.of(new Remark("Recovering")),
            Optional.empty());

        JsonAdaptedPerson adapted = new JsonAdaptedPerson(client);
        assertEquals(client, adapted.toModelType());
    }

    @Test
    public void constructor_fromUnknownTypeToModelType_throwsIllegalValueException() {
        Person unknown = new UnknownPerson(new Name("Unknown"), new Phone("81234567"), Set.of());
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(unknown);
        assertThrows(IllegalValueException.class, JsonAdaptedPerson.INVALID_TYPE_MESSAGE_FORMAT, adapted::toModelType);
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson("trainer", INVALID_NAME, VALID_PHONE, VALID_EMAIL,
                        null, null, 0, 0, null, null, null, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("trainer", null, VALID_PHONE, VALID_EMAIL,
                null, null, 0, 0, null, null, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson("trainer", VALID_NAME, INVALID_PHONE, VALID_EMAIL,
                        null, null, 0, 0, null, null, null, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("trainer", VALID_NAME, null, VALID_EMAIL,
                null, null, 0, 0, null, null, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson("trainer", VALID_NAME, VALID_PHONE, INVALID_EMAIL,
                        null, null, 0, 0, null, null, null, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("trainer", VALID_NAME, VALID_PHONE, null,
                null, null, 0, 0, null, null, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson("trainer", VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        null, null, 0, 0, null, null, null, invalidTags);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_nullTypeWithEmail_fallsBackToTrainer() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_NAME, VALID_PHONE, VALID_EMAIL,
                null, null, 0, 0, null, null, null, VALID_TAGS);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_nullTypeWithTrainerPhone_fallsBackToClient() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_CLIENT_NAME, VALID_CLIENT_PHONE, null,
                VALID_TRAINER_PHONE, VALID_TRAINER_NAME, 0, 0, null, null, null, VALID_TAGS);

        Client expected = new Client(new Name(VALID_CLIENT_NAME), new Phone(VALID_CLIENT_PHONE),
                new Phone(VALID_TRAINER_PHONE), new Name(VALID_TRAINER_NAME),
                BENSON.getTags(), 0, 0, Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(expected, person.toModelType());
    }

    @Test
    public void toModelType_invalidTrainerPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_CLIENT_NAME, VALID_CLIENT_PHONE, null,
                INVALID_PHONE, VALID_TRAINER_NAME, 0, 0, null, null, null, VALID_TAGS);
        assertThrows(IllegalValueException.class, Phone.MESSAGE_CONSTRAINTS, person::toModelType);
    }

    @Test
    public void toModelType_nullTrainerPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_CLIENT_NAME, VALID_CLIENT_PHONE, null,
                null, VALID_TRAINER_NAME, 0, 0, null, null, null, VALID_TAGS);
        assertThrows(IllegalValueException.class,
                String.format(MISSING_FIELD_MESSAGE_FORMAT, "trainerPhone"), person::toModelType);
    }

    @Test
    public void toModelType_invalidTrainerName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_CLIENT_NAME, VALID_CLIENT_PHONE, null,
                VALID_TRAINER_PHONE, INVALID_NAME, 0, 0, null, null, null, VALID_TAGS);
        assertThrows(IllegalValueException.class, Name.MESSAGE_CONSTRAINTS, person::toModelType);
    }

    @Test
    public void toModelType_nullTrainerName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_CLIENT_NAME, VALID_CLIENT_PHONE, null,
                VALID_TRAINER_PHONE, null, 0, 0, null, null, null, VALID_TAGS);
        assertThrows(IllegalValueException.class,
                String.format(MISSING_FIELD_MESSAGE_FORMAT, "trainerName"), person::toModelType);
    }

    @Test
    public void toModelType_invalidWorkoutFocus_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_CLIENT_NAME, VALID_CLIENT_PHONE, null,
                VALID_TRAINER_PHONE, VALID_TRAINER_NAME, 0, 0, INVALID_WORKOUT_FOCUS, null, null, VALID_TAGS);
        assertThrows(IllegalValueException.class, WorkoutFocus.MESSAGE_CONSTRAINTS, person::toModelType);
    }

    @Test
    public void toModelType_invalidRemark_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_CLIENT_NAME, VALID_CLIENT_PHONE, null,
                VALID_TRAINER_PHONE, VALID_TRAINER_NAME, 0, 0, null, INVALID_REMARK, null, VALID_TAGS);
        assertThrows(IllegalValueException.class, Remark.MESSAGE_CONSTRAINTS, person::toModelType);
    }

    @Test
    public void toModelType_invalidType_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("unknown", VALID_NAME, VALID_PHONE, null,
                null, null, 0, 0, null, null, null, VALID_TAGS);
        assertThrows(IllegalValueException.class, JsonAdaptedPerson.INVALID_TYPE_MESSAGE_FORMAT, person::toModelType);
    }

    @Test
    public void toModelType_pastValidity_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_CLIENT_NAME, VALID_CLIENT_PHONE, null,
                VALID_TRAINER_PHONE, VALID_TRAINER_NAME, 0, 0, null, null, "2000-01-01", VALID_TAGS);
        assertThrows(IllegalValueException.class, Validity.MESSAGE_PAST_DATE, person::toModelType);
    }

    private static class UnknownPerson extends Person {
        UnknownPerson(Name name, Phone phone, Set<Tag> tags) {
            super(name, phone, tags);
        }
    }
}
