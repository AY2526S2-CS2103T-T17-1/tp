package seedu.address.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GuiSettingsTest {
    @Test
    public void toStringMethod() {
        GuiSettings guiSettings = new GuiSettings();
        String expected = GuiSettings.class.getCanonicalName() + "{windowWidth=" + guiSettings.getWindowWidth()
                + ", windowHeight=" + guiSettings.getWindowHeight() + ", windowCoordinates="
                + guiSettings.getWindowCoordinates() + "}";
        assertEquals(expected, guiSettings.toString());
    }

    @Test
    public void equalsAndHashCode() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);

        assertTrue(guiSettings.equals(guiSettings));
        assertTrue(guiSettings.equals(new GuiSettings(1, 2, 3, 4)));
        assertEquals(guiSettings.hashCode(), new GuiSettings(1, 2, 3, 4).hashCode());

        assertFalse(guiSettings.equals(null));
        assertFalse(guiSettings.equals(5));
        assertFalse(guiSettings.equals(new GuiSettings(1, 2, 3, 5)));
    }
}
