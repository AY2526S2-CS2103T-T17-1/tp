package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Person's remark in the address book.
 * Guarantees: immutable; details are present and not null.
 */
public class Remark {

    public final String value;

    /**
     * Constructs a {@code Remark}.
     */
    public Remark(String remark) {
        requireNonNull(remark);
        value = remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("value", value)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Remark)) {
            return false;
        }

        Remark otherRemark = (Remark) other;
        return value.equals(otherRemark.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
