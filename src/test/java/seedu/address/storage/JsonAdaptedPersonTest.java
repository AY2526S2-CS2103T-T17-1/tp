package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Trainer;
import seedu.address.model.person.WorkoutFocus;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = ((Trainer) BENSON).getEmail().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    // --- Trainer tests (existing) ---

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson("trainer", INVALID_NAME, VALID_PHONE, VALID_EMAIL,
                        null, null, 0, 0, null, null, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("trainer", null, VALID_PHONE, VALID_EMAIL,
                null, null, 0, 0, null, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson("trainer", VALID_NAME, INVALID_PHONE, VALID_EMAIL,
                        null, null, 0, 0, null, null, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("trainer", VALID_NAME, null, VALID_EMAIL,
                null, null, 0, 0, null, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson("trainer", VALID_NAME, VALID_PHONE, INVALID_EMAIL,
                        null, null, 0, 0, null, null, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("trainer", VALID_NAME, VALID_PHONE, null,
                null, null, 0, 0, null, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson("trainer", VALID_NAME, VALID_PHONE, VALID_EMAIL,
                        null, null, 0, 0, null, null, invalidTags);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    // --- Client tests (new) ---

    @Test
    public void toModelType_validClientDetails_returnsClient() throws Exception {
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
                new Phone("91234567"), new Name("John"), new HashSet<>());
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(client);
        assertEquals(client, adapted.toModelType());
    }

    @Test
    public void toModelType_validClientWithOptionalFields_returnsClient() throws Exception {
        Client client = new Client(new Name("Alice"), new Phone("81234567"),
                new Phone("91234567"), new Name("John"), new HashSet<>(),
                2000, 1500, Optional.of(new WorkoutFocus("Chest")), Optional.of(new Remark("Note")));
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(client);
        assertEquals(client, adapted.toModelType());
    }

    @Test
    public void toModelType_clientNullTrainerPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_NAME, VALID_PHONE, null,
                null, "John", 0, 0, null, null, new ArrayList<>());
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "trainerPhone");
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_clientInvalidTrainerPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_NAME, VALID_PHONE, null,
                "+651234", "John", 0, 0, null, null, new ArrayList<>());
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_clientNullTrainerName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_NAME, VALID_PHONE, null,
                "91234567", null, 0, 0, null, null, new ArrayList<>());
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "trainerName");
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_clientInvalidTrainerName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_NAME, VALID_PHONE, null,
                "91234567", "J@hn", 0, 0, null, null, new ArrayList<>());
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_clientInvalidWorkoutFocus_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_NAME, VALID_PHONE, null,
                "91234567", "John", 0, 0, "Ch3st", null, new ArrayList<>());
        String expectedMessage = WorkoutFocus.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_clientInvalidRemark_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("client", VALID_NAME, VALID_PHONE, null,
                "91234567", "John", 0, 0, null, "   ", new ArrayList<>());
        String expectedMessage = Remark.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidType_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson("unknown", VALID_NAME, VALID_PHONE, null,
                null, null, 0, 0, null, null, new ArrayList<>());
        assertThrows(IllegalValueException.class, JsonAdaptedPerson.INVALID_TYPE_MESSAGE_FORMAT,
                person::toModelType);
    }
}
