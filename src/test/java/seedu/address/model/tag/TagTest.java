package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        assertFalse(Tag.isValidTagName(""));
        assertFalse(Tag.isValidTagName("#friend"));
        assertTrue(Tag.isValidTagName("friends"));
    }

    @Test
    public void equalsHashCodeToString() {
        Tag tag = new Tag("friends");

        assertTrue(tag.equals(tag));
        assertTrue(tag.equals(new Tag("friends")));
        assertFalse(tag.equals(null));
        assertFalse(tag.equals(5));
        assertFalse(tag.equals(new Tag("colleagues")));

        assertEquals(tag.hashCode(), new Tag("friends").hashCode());
        assertEquals("[friends]", tag.toString());
    }

}
