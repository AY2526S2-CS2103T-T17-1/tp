package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.AppUtil;

/**
 * Represents a Client's workout focus (primary muscle group focus). Guarantees:
 * immutable; is valid as declared in {@link #isValidWorkoutFocus(String)}.
 */
public class WorkoutFocus {

    public static final String MESSAGE_CONSTRAINTS =
            "Focus string must only contain letters, and words may be separated by single spaces (e.g., Upper Body).";
    private static final String VALIDATION_REGEX = "[A-Za-z]+(\\s[A-Za-z]+)*";

    private final String value;

    /**
     * Constructs a {@code WorkoutFocus}.
     *
     * @param workoutFocus A valid workout focus.
     */
    public WorkoutFocus(String workoutFocus) {
        requireNonNull(workoutFocus);
        AppUtil.checkArgument(isValidWorkoutFocus(workoutFocus), MESSAGE_CONSTRAINTS);
        this.value = workoutFocus;
    }

    /**
     * Returns the workout focus value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns true if a given string is a valid workout focus.
     */
    public static boolean isValidWorkoutFocus(String test) {
        return test != null && test.matches(VALIDATION_REGEX);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof WorkoutFocus
                && value.equals(((WorkoutFocus) other).value));
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
