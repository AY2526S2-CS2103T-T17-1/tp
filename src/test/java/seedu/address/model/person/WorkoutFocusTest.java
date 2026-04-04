package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class WorkoutFocusTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WorkoutFocus(null));
    }

    @Test
    public void constructor_invalidWorkoutFocus_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WorkoutFocus(""));
        assertThrows(IllegalArgumentException.class, () -> new WorkoutFocus("Ch3st"));
    }

    @Test
    public void constructor_validWorkoutFocus_success() {
        WorkoutFocus focus = new WorkoutFocus("Chest");
        assertEquals("Chest", focus.getValue());
    }

    @Test
    public void isValidWorkoutFocus() {
        // null -> false
        assertFalse(WorkoutFocus.isValidWorkoutFocus(null));

        // empty string -> false
        assertFalse(WorkoutFocus.isValidWorkoutFocus(""));

        // contains digits -> false
        assertFalse(WorkoutFocus.isValidWorkoutFocus("Ch3st"));

        // contains special chars -> false
        assertFalse(WorkoutFocus.isValidWorkoutFocus("Chest!"));

        // valid: letters only
        assertTrue(WorkoutFocus.isValidWorkoutFocus("Chest"));
        assertTrue(WorkoutFocus.isValidWorkoutFocus("Back"));
        assertTrue(WorkoutFocus.isValidWorkoutFocus("a"));
    }

    @Test
    public void equals() {
        WorkoutFocus focus = new WorkoutFocus("Chest");

        // same object -> true
        assertTrue(focus.equals(focus));

        // same value -> true
        assertTrue(focus.equals(new WorkoutFocus("Chest")));

        // null -> false
        assertFalse(focus.equals(null));

        // different type -> false
        assertFalse(focus.equals("Chest"));

        // different value -> false
        assertFalse(focus.equals(new WorkoutFocus("Back")));
    }

    @Test
    public void hashCode_sameValue_sameHash() {
        WorkoutFocus f1 = new WorkoutFocus("Chest");
        WorkoutFocus f2 = new WorkoutFocus("Chest");
        assertEquals(f1.hashCode(), f2.hashCode());
    }

    @Test
    public void toStringMethod() {
        WorkoutFocus focus = new WorkoutFocus("Chest");
        assertEquals("Chest", focus.toString());
    }
}
