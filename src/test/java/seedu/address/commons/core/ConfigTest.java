package seedu.address.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.logging.Level;

import org.junit.jupiter.api.Test;

public class ConfigTest {

    @Test
    public void toStringMethod() {
        Config config = new Config();
        String expected = Config.class.getCanonicalName() + "{logLevel=" + config.getLogLevel()
                + ", userPrefsFilePath=" + config.getUserPrefsFilePath() + "}";
        assertEquals(expected, config.toString());
    }

    @Test
    public void equalsMethod() {
        Config defaultConfig = new Config();
        assertNotNull(defaultConfig);
        assertTrue(defaultConfig.equals(defaultConfig));

        assertFalse(defaultConfig.equals(null));
        assertFalse(defaultConfig.equals(5));

        Config same = new Config();
        assertTrue(defaultConfig.equals(same));
        assertEquals(defaultConfig.hashCode(), same.hashCode());

        Config differentLevel = new Config();
        differentLevel.setLogLevel(Level.FINE);
        assertFalse(defaultConfig.equals(differentLevel));

        Config differentPath = new Config();
        differentPath.setUserPrefsFilePath(Paths.get("different.json"));
        assertFalse(defaultConfig.equals(differentPath));
    }
}
