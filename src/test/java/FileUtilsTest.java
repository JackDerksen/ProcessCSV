import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class FileUtilsTest {

    @Test
    @DisplayName("buildFilePath should append filename to base directory")
    public void testBuildFilePath() {
        // Arrange
        String fileName = "test.csv";
        String expectedPath = "src/main/resources/test.csv";

        // Act
        Path result = FileUtils.buildFilePath(fileName);

        // Assert
        assertEquals(Paths.get(expectedPath), result);
    }

    @ParameterizedTest
    @DisplayName("buildFilePath should work with various filenames")
    @ValueSource(strings = {"data.csv", "property_assessments.csv", "complex-file-name.csv", "nested/path/file.csv"})
    public void testBuildFilePathWithVariousNames(String fileName) {
        // Arrange
        String expectedPath = "src/main/resources/" + fileName;

        // Act
        Path result = FileUtils.buildFilePath(fileName);

        // Assert
        assertEquals(Paths.get(expectedPath), result);
    }

    @Test
    @DisplayName("buildFilePath should handle empty filename")
    public void testBuildFilePathWithEmptyString() {
        // Arrange
        String fileName = "";
        String expectedPath = "src/main/resources/";

        // Act
        Path result = FileUtils.buildFilePath(fileName);

        // Assert
        assertEquals(Paths.get(expectedPath), result);
    }

    @Test
    @DisplayName("buildFilePath should handle null filename")
    public void testBuildFilePathWithNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> FileUtils.buildFilePath(null));
    }
}