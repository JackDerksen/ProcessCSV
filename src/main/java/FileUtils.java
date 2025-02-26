import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    private static final String BASE_DIR = "src/main/resources/";

    public static Path buildFilePath(String fileName) {
        return Paths.get(BASE_DIR + fileName);
    }
}
