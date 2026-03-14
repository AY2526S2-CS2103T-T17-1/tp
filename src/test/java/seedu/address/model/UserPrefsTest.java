package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;

public class UserPrefsTest {

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        UserPrefs userPref = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPref.setGuiSettings(null));
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        UserPrefs userPrefs = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPrefs.setAddressBookFilePath(null));
    }

    @Test
    public void resetData_andEqualsHashCode() {
        UserPrefs prefs = new UserPrefs();
        prefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        prefs.setAddressBookFilePath(Paths.get("data", "test.json"));

        UserPrefs copy = new UserPrefs(prefs);
        assertEquals(prefs, copy);
        assertEquals(prefs.hashCode(), copy.hashCode());
        assertTrue(copy.toString().contains("Gui Settings"));

        assertTrue(prefs.equals(prefs));
        assertFalse(prefs.equals(null));
        assertFalse(prefs.equals(5));

        UserPrefs different = new UserPrefs();
        different.setAddressBookFilePath(Paths.get("other.json"));
        assertFalse(prefs.equals(different));
    }

}
