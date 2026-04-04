package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a Client's membership validity date in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidValidity(String)}
 */
public class Validity {

    public static final String MESSAGE_CONSTRAINTS = "Validity should be a valid date in the format YYYY-MM-DD.";
    public static final String MESSAGE_PAST_DATE = "Validity date must not be in the past.";

    public final String value;

    /**
     * Constructs a {@code Validity}.
     *
     * @param validity A valid date string.
     */
    public Validity(String validity) {
        requireNonNull(validity);
        checkArgument(isValidValidity(validity), MESSAGE_CONSTRAINTS);
        value = validity;
    }

    /**
     * Returns true if a given string is a valid validity date format.
     */
    public static boolean isValidValidity(String test) {
        try {
            LocalDate.parse(test, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Returns true if the given validity date string is today or in the future.
     * Must only be called after {@link #isValidValidity(String)} confirms the format is correct.
     */
    public static boolean isNotPastDate(String test) {
        return !LocalDate.parse(test, DateTimeFormatter.ISO_LOCAL_DATE).isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Validity)) {
            return false;
        }

        Validity otherValidity = (Validity) other;
        return value.equals(otherValidity.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
