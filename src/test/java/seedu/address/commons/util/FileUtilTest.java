package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class FileUtilTest {

    @TempDir
    public Path temporaryFolder;

    @Test
    public void constructor() {
        assertNotNull(new FileUtil());
    }

    @Test
    public void isValidPath() {
        // valid path
        assertTrue(FileUtil.isValidPath("valid/file/path"));

        // invalid path
        assertFalse(FileUtil.isValidPath("a\0"));

        // null path -> throws NullPointerException
        assertThrows(NullPointerException.class, () -> FileUtil.isValidPath(null));
    }

    @Test
    public void createFile_whenFileExists_returnsWithoutError() throws Exception {
        Path file = temporaryFolder.resolve("existing.txt");
        Files.createFile(file);

        FileUtil.createFile(file);
        assertTrue(Files.exists(file));
    }

    @Test
    public void createIfMissing_createsParentDirectoriesAndFile() throws Exception {
        Path nestedFile = temporaryFolder.resolve("nested").resolve("file.txt");
        FileUtil.createIfMissing(nestedFile);
        assertTrue(Files.exists(nestedFile));

        // idempotent when file exists
        FileUtil.createIfMissing(nestedFile);
        assertTrue(Files.exists(nestedFile));
    }

}
