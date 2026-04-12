package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Client;
import seedu.address.model.person.Person;
import seedu.address.model.person.Trainer;

public class SampleDataUtilTest {

    @Test
    public void constructor() {
        SampleDataUtil util = new SampleDataUtil();
        assertEquals(SampleDataUtil.class, util.getClass());
    }

    @Test
    public void getSamplePersons_containsTrainersAndClients() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertEquals(6, persons.length);
        assertTrue(persons[0] instanceof Trainer);
        assertTrue(persons[1] instanceof Trainer);
        assertTrue(persons[2] instanceof Trainer);
        assertTrue(persons[3] instanceof Client);
        assertTrue(persons[4] instanceof Client);
        assertTrue(persons[5] instanceof Client);
    }

    @Test
    public void getSampleAddressBook_containsAllSamplePersons() {
        ReadOnlyAddressBook ab = SampleDataUtil.getSampleAddressBook();
        assertEquals(6, ab.getPersonList().size());
    }

}
