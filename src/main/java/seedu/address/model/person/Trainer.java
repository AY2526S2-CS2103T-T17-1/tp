package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Trainer in the system.
 */
public class Trainer extends Person {

    private final Email email;

    /**
     * Every field must be present and not null.
     */
    public Trainer(Name name, Phone phone, Email email, Set<Tag> tags) {
        super(name, phone, tags);
        requireAllNonNull(email);
        this.email = email;
    }

    public Email getEmail() {
        return email;
    }

    /**
     * Returns true if both trainers have the same phone or email.
     * Trainers must not share a phone or email.
     */
    @Override
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }
        if (!(otherPerson instanceof Trainer)) {
            return super.isSamePerson(otherPerson);
        }

        Trainer otherTrainer = (Trainer) otherPerson;
        return otherTrainer.getPhone().equals(getPhone()) || otherTrainer.getEmail().equals(getEmail());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Trainer)) {
            return false;
        }

        Trainer otherTrainer = (Trainer) other;
        return name.equals(otherTrainer.name)
                && phone.equals(otherTrainer.phone)
                && email.equals(otherTrainer.email)
                && tags.equals(otherTrainer.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("tags", tags)
                .toString();
    }
}
