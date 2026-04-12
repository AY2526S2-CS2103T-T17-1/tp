package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Trainer;
import seedu.address.model.person.WorkoutFocus;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_TRAINER_NAME = "Bob";
    public static final String DEFAULT_TRAINER_PHONE = "12345678";

    private Name name;
    private Phone phone;
    private Email email;
    private Name trainerName;
    private Phone trainerPhone;
    private WorkoutFocus workoutFocus;
    private Remark remark;
    private Set<Tag> tags;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        tags = new HashSet<>(personToCopy.getTags());
        if (personToCopy instanceof Trainer) {
            email = ((Trainer) personToCopy).getEmail();
        } else if (personToCopy instanceof Client) {
            trainerPhone = ((Client) personToCopy).getTrainerPhone();
            trainerName = ((Client) personToCopy).getTrainerName();
            workoutFocus = ((Client) personToCopy).getWorkoutFocus().orElse(null);
            remark = ((Client) personToCopy).getRemark().orElse(null);
        }
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = new HashSet<>();
        for (String tag : tags) {
            this.tags.add(new Tag(tag));
        }
        return this;
    }

    /**
     * Sets the {@code trainerName} of the {@code Person} that we are building.
     */
    public PersonBuilder withTrainerName(String trainerName) {
        this.trainerName = new Name(trainerName);
        return this;
    }

    /**
     * Sets the {@code trainerPhone} of the {@code Person} that we are building.
     */
    public PersonBuilder withTrainerPhone(String trainerPhone) {
        this.trainerPhone = new Phone(trainerPhone);
        return this;
    }

    /**
     * Sets the {@code phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Builds and returns the {@code Person} object (either a Trainer or Client).
     */
    public Person build() {
        if (trainerPhone != null && trainerName != null) {
            return new Client(name, phone, trainerPhone, trainerName, tags, 0, 0,
                    java.util.Optional.ofNullable(workoutFocus),
                    java.util.Optional.ofNullable(remark));
        }
        return new Trainer(name, phone, email, tags);
    }

}
