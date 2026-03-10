package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Client in the system.
 */
public class Client extends Person {

    private final Phone trainerPhone;
    private final Name trainerName;

    /**
     * Every field must be present and not null.
     */
    public Client(Name name, Phone phone, Phone trainerPhone, Name trainerName, Set<Tag> tags) {
        super(name, phone, tags);
        requireAllNonNull(trainerPhone, trainerName);
        this.trainerPhone = trainerPhone;
        this.trainerName = trainerName;
    }

    public Phone getTrainerPhone() {
        return trainerPhone;
    }

    public Name getTrainerName() {
        return trainerName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Client)) {
            return false;
        }

        Client otherClient = (Client) other;
        return name.equals(otherClient.name)
                && phone.equals(otherClient.phone)
                && trainerPhone.equals(otherClient.trainerPhone)
                && trainerName.equals(otherClient.trainerName)
                && tags.equals(otherClient.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, trainerPhone, trainerName, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("trainerPhone", trainerPhone)
                .add("trainerName", trainerName)
                .add("tags", tags)
                .toString();
    }
}
