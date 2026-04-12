package seedu.address.model.util;

import java.util.Set;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Trainer;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Trainer(
                    new Name("Alex Yeoh"),
                    new Phone("87438807"),
                    new Email("alexyeoh@example.com"),
                    Set.of()),
            new Trainer(
                    new Name("Bernice Yu"),
                    new Phone("99272758"),
                    new Email("berniceyu@example.com"),
                    Set.of()),
            new Trainer(
                    new Name("Charlotte Oliveiro"),
                    new Phone("93210283"),
                    new Email("charlotte@example.com"),
                    Set.of()),
            new Client(
                    new Name("David Li"),
                    new Phone("91031282"),
                    new Phone("87438807"),
                    new Name("Alex Yeoh"),
                    Set.of()),
            new Client(
                    new Name("Irfan Ibrahim"),
                    new Phone("92492021"),
                    new Phone("99272758"),
                    new Name("Bernice Yu"),
                    Set.of()),
            new Client(
                    new Name("Roy Balakrishnan"),
                    new Phone("92624417"),
                    new Phone("93210283"),
                    new Name("Charlotte Oliveiro"),
                    Set.of())
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

}
