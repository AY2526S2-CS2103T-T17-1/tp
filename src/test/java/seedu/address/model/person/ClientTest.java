package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;

public class ClientTest {

    private static final Name NAME = new Name("Alice");
    private static final Phone PHONE = new Phone("81234567");
    private static final Phone TRAINER_PHONE = new Phone("91234567");
    private static final Name TRAINER_NAME = new Name("John");
    private static final Set<Tag> TAGS = new HashSet<>();

    @Test
    public void constructor_minimalFields_defaultCalories() {
        Client client = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS);
        assertEquals(0, client.getCalorieTarget());
        assertEquals(0, client.getCalorieIntake());
        assertEquals(Optional.empty(), client.getWorkoutFocus());
        assertEquals(Optional.empty(), client.getRemark());
    }

    @Test
    public void constructor_withCalories_setsCalories() {
        Client client = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS, 2000, 1500);
        assertEquals(2000, client.getCalorieTarget());
        assertEquals(1500, client.getCalorieIntake());
        assertEquals(Optional.empty(), client.getWorkoutFocus());
        assertEquals(Optional.empty(), client.getRemark());
    }

    @Test
    public void constructor_withAllFields_setsAllFields() {
        WorkoutFocus focus = new WorkoutFocus("Chest");
        Remark remark = new Remark("Needs follow-up");
        Client client = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS,
                2000, 1500, Optional.of(focus), Optional.of(remark));
        assertEquals(Optional.of(focus), client.getWorkoutFocus());
        assertEquals(Optional.of(remark), client.getRemark());
    }

    @Test
    public void constructor_nullOptionals_defaultsToEmpty() {
        Client client = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS,
                0, 0, null, null);
        assertEquals(Optional.empty(), client.getWorkoutFocus());
        assertEquals(Optional.empty(), client.getRemark());
    }

    @Test
    public void getTrainerPhone_returnsCorrectPhone() {
        Client client = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS);
        assertEquals(TRAINER_PHONE, client.getTrainerPhone());
    }

    @Test
    public void getTrainerName_returnsCorrectName() {
        Client client = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS);
        assertEquals(TRAINER_NAME, client.getTrainerName());
    }

    @Test
    public void equals() {
        Client client = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS,
                2000, 1500, Optional.of(new WorkoutFocus("Chest")), Optional.of(new Remark("Note")));

        // same object -> true
        assertTrue(client.equals(client));

        // same values -> true
        Client clientCopy = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS,
                2000, 1500, Optional.of(new WorkoutFocus("Chest")), Optional.of(new Remark("Note")));
        assertTrue(client.equals(clientCopy));

        // null -> false
        assertFalse(client.equals(null));

        // different type -> false
        assertFalse(client.equals(5));

        // different name -> false
        Client differentName = new Client(new Name("Bob"), PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS,
                2000, 1500, Optional.of(new WorkoutFocus("Chest")), Optional.of(new Remark("Note")));
        assertFalse(client.equals(differentName));

        // different phone -> false
        Client differentPhone = new Client(NAME, new Phone("99999999"), TRAINER_PHONE, TRAINER_NAME, TAGS,
                2000, 1500, Optional.of(new WorkoutFocus("Chest")), Optional.of(new Remark("Note")));
        assertFalse(client.equals(differentPhone));

        // different trainer phone -> false
        Client differentTrainerPhone = new Client(NAME, PHONE, new Phone("88888888"), TRAINER_NAME, TAGS,
                2000, 1500, Optional.of(new WorkoutFocus("Chest")), Optional.of(new Remark("Note")));
        assertFalse(client.equals(differentTrainerPhone));

        // different trainer name -> false
        Client differentTrainerName = new Client(NAME, PHONE, TRAINER_PHONE, new Name("Mike"), TAGS,
                2000, 1500, Optional.of(new WorkoutFocus("Chest")), Optional.of(new Remark("Note")));
        assertFalse(client.equals(differentTrainerName));

        // different calorie target -> false
        Client differentCalTarget = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS,
                3000, 1500, Optional.of(new WorkoutFocus("Chest")), Optional.of(new Remark("Note")));
        assertFalse(client.equals(differentCalTarget));

        // different calorie intake -> false
        Client differentCalIntake = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS,
                2000, 500, Optional.of(new WorkoutFocus("Chest")), Optional.of(new Remark("Note")));
        assertFalse(client.equals(differentCalIntake));

        // different workout focus -> false
        Client differentFocus = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS,
                2000, 1500, Optional.of(new WorkoutFocus("Back")), Optional.of(new Remark("Note")));
        assertFalse(client.equals(differentFocus));

        // different remark -> false
        Client differentRemark = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS,
                2000, 1500, Optional.of(new WorkoutFocus("Chest")), Optional.of(new Remark("Other")));
        assertFalse(client.equals(differentRemark));
    }

    @Test
    public void hashCode_sameValues_sameHash() {
        Client c1 = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS, 2000, 1500,
                Optional.of(new WorkoutFocus("Chest")), Optional.of(new Remark("Note")));
        Client c2 = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS, 2000, 1500,
                Optional.of(new WorkoutFocus("Chest")), Optional.of(new Remark("Note")));
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    public void hashCode_differentValues_differentHash() {
        Client c1 = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS);
        Client c2 = new Client(new Name("Bob"), PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS);
        assertNotEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    public void toStringMethod() {
        Client client = new Client(NAME, PHONE, TRAINER_PHONE, TRAINER_NAME, TAGS, 2000, 1500,
                Optional.of(new WorkoutFocus("Chest")), Optional.of(new Remark("Note")));
        String result = client.toString();
        assertTrue(result.contains("name=" + NAME));
        assertTrue(result.contains("phone=" + PHONE));
        assertTrue(result.contains("trainerPhone=" + TRAINER_PHONE));
        assertTrue(result.contains("trainerName=" + TRAINER_NAME));
        assertTrue(result.contains("calorieTarget=2000"));
        assertTrue(result.contains("calorieIntake=1500"));
    }
}
