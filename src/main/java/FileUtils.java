import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Simple utility class for file path operations, providing methods to
 * build a file path to the resource directory (contains the data csv)
 */

public class FileUtils {
    private static final String BASE_DIR = "src/main/resources/";

    public static Path buildFilePath(String fileName) {
        return Paths.get(BASE_DIR + fileName);
    }
}
