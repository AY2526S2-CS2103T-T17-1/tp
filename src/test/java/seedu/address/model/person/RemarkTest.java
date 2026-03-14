package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RemarkTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Remark(null));
    }

    @Test
    public void constructor_invalidRemark_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Remark(""));
        assertThrows(IllegalArgumentException.class, () -> new Remark("   "));
    }

    @Test
    public void constructor_validRemark_success() {
        Remark remark = new Remark("Recovering from ACL surgery");
        assertEquals("Recovering from ACL surgery", remark.value);
    }

    @Test
    public void isValidRemark() {
        // null -> false
        assertFalse(Remark.isValidRemark(null));

        // empty string -> false
        assertFalse(Remark.isValidRemark(""));

        // whitespace only -> false
        assertFalse(Remark.isValidRemark("   "));

        // valid remarks
        assertTrue(Remark.isValidRemark("Has knee injury"));
        assertTrue(Remark.isValidRemark("A"));
        assertTrue(Remark.isValidRemark("Client prefers morning sessions, no heavy lifting"));
    }

    @Test
    public void equals() {
        Remark remark = new Remark("Some note");

        // same object -> true
        assertTrue(remark.equals(remark));

        // same value -> true
        assertTrue(remark.equals(new Remark("Some note")));

        // null -> false
        assertFalse(remark.equals(null));

        // different type -> false
        assertFalse(remark.equals("Some note"));

        // different value -> false
        assertFalse(remark.equals(new Remark("Other note")));
    }

    @Test
    public void hashCode_sameValue_sameHash() {
        Remark remark1 = new Remark("Note");
        Remark remark2 = new Remark("Note");
        assertEquals(remark1.hashCode(), remark2.hashCode());
    }

    @Test
    public void toStringMethod() {
        Remark remark = new Remark("Note");
        assertEquals("Note", remark.toString());
    }
}
