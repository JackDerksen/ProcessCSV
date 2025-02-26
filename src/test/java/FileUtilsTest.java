import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {
    /// Verifies that the filepath is being constructed properly
    @Test
    void buildFilePath() {
        String fileName = "test.csv";

        Path expectedPath = Path.of("src/main/resources/test.csv");
        Path actualPath = FileUtils.buildFilePath(fileName);

        assertEquals(expectedPath.toString(), actualPath.toString());
    }
}