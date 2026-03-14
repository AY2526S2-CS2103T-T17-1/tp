package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().add(new Tag("friends")));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same phone, different email and name -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).withName(VALID_NAME_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same email, different phone and name -> returns true
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withName(VALID_NAME_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different phone and email, same name -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different phone and email, different name -> returns false
        Person editedBob = new PersonBuilder(BOB).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.isSamePerson(editedBob));
    }

    @Test
    public void isSamePerson_nonTrainerNonClient_usesPhoneOnly() {
        Person person = new TestPerson(new Name("Alice"), new Phone("81234567"), new HashSet<>());

        // same object
        assertTrue(person.isSamePerson(person));

        // null
        assertFalse(person.isSamePerson(null));

        // same phone, different name
        Person samePhoneDifferentName = new TestPerson(new Name("Bob"), new Phone("81234567"), new HashSet<>());
        assertTrue(person.isSamePerson(samePhoneDifferentName));

        // different phone
        Person differentPhone = new TestPerson(new Name("Alice"), new Phone("81230000"), new HashSet<>());
        assertFalse(person.isSamePerson(differentPhone));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void equals_nonTrainerNonClient_comparesNamePhoneAndTags() {
        Person person = new TestPerson(new Name("Alice"), new Phone("81234567"), new HashSet<>());

        // same object
        assertTrue(person.equals(person));

        // non-Person type
        Object notPerson = "not a person";
        assertFalse(person.equals(notPerson));

        // different type
        assertFalse(person.equals(new PersonBuilder().build()));

        // same values
        Person sameValues = new TestPerson(new Name("Alice"), new Phone("81234567"), new HashSet<>());
        assertTrue(person.equals(sameValues));
        assertEquals(person.hashCode(), sameValues.hashCode());

        // different tags
        HashSet<Tag> tags = new HashSet<>();
        tags.add(new Tag("friends"));
        Person differentTags = new TestPerson(new Name("Alice"), new Phone("81234567"), tags);
        assertFalse(person.equals(differentTags));

        assertTrue(person.toString().contains("Alice"));
    }

    @Test
    public void toStringMethod() {
        String expected = Trainer.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ((Trainer) ALICE).getEmail()
                + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());

        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertEquals(ALICE.hashCode(), aliceCopy.hashCode());
    }

    private static class TestPerson extends Person {
        TestPerson(Name name, Phone phone, java.util.Set<Tag> tags) {
            super(name, phone, tags);
        }
    }
}
