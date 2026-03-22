package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.AppUtil;

/**
 * Represents a remark attached to a Client. Guarantees: immutable; is valid as
 * declared in {@link #isValidRemark(String)}.
 */
public class Remark {

    public static final String MESSAGE_CONSTRAINTS = "Remark cannot be empty.";

    private final String value;

    /**
     * Constructs a {@code Remark}.
     *
     * @param remark A valid remark.
     */
    public Remark(String remark) {
        requireNonNull(remark);
        AppUtil.checkArgument(isValidRemark(remark), MESSAGE_CONSTRAINTS);
        this.value = remark;
    }

    /**
     * Returns the remark value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns true if a given string is a valid remark.
     */
    public static boolean isValidRemark(String test) {
        return test != null && !test.trim().isEmpty();
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
                || (other instanceof Remark
                && value.equals(((Remark) other).value));
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
