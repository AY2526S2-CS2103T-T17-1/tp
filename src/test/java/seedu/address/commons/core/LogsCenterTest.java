package seedu.address.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class LogsCenterTest {

    @TempDir
    public Path temporaryFolder;

    @Test
    public void constructor() {
        assertNotNull(new LogsCenter());
    }

    @Test
    public void getLogger_withClass_returnsLogger() {
        Logger logger = LogsCenter.getLogger(LogsCenterTest.class);
        assertNotNull(logger);
        assertEquals("ab3." + LogsCenterTest.class.getSimpleName(), logger.getName());
    }

    @Test
    public void getLogger_withName_returnsLogger() {
        Logger logger = LogsCenter.getLogger("Custom");
        assertNotNull(logger);
        assertEquals("ab3.Custom", logger.getName());
    }

    @Test
    public void init_setsLogLevel() {
        Config config = new Config();
        config.setLogLevel(Level.FINE);
        LogsCenter.init(config);

        Logger logger = LogsCenter.getLogger("AfterInit");
        assertNotNull(logger);
    }

    @Test
    public void setBaseLogger_fileHandlerCannotBeCreated_catchesIoException() throws Exception {
        try {
            LogsCenter.setFileHandlerFactoryForTesting(() -> {
                throw new IOException("dummy");
            });
            LogsCenter.resetBaseLoggerForTesting();

            Logger baseLogger = LogsCenter.getBaseLoggerForTesting();
            assertNotNull(baseLogger);
            assertEquals(1, baseLogger.getHandlers().length);
        } finally {
            LogsCenter.resetFileHandlerFactoryForTesting();
            LogsCenter.resetBaseLoggerForTesting();

            Logger baseLogger = LogsCenter.getBaseLoggerForTesting();
            assertTrue(baseLogger.getHandlers().length >= 1);
        }
    }
}
