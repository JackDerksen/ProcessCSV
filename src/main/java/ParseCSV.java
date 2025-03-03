import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for parsing CSV files, handling special cases like
 * commas within parentheses to ensure correct field splitting.
 */

public class ParseCSV {
    public static String[][] readData(Path filePath) throws IOException {
        List<String[]> dataList = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            // Skip header line
            reader.readLine();

            // Split the lines, add complete data rows to dataList
            String line;
            while ((line = reader.readLine()) != null) {
                // Split on the commas that aren't inside parentheses (was causing issues otherwise)
                List<String> fields = new ArrayList<>();
                StringBuilder currentField = new StringBuilder();
                boolean insideParens = false;

                for (char c : line.toCharArray()) {
                    if (c == '(') {
                        insideParens = true;
                        currentField.append(c);
                    } else if (c == ')') {
                        insideParens = false;
                        currentField.append(c);
                    } else if (c == ',' && !insideParens) {
                        fields.add(currentField.toString().trim());
                        currentField = new StringBuilder();
                    } else {
                        currentField.append(c);
                    }
                }
                fields.add(currentField.toString().trim());

                dataList.add(fields.toArray(new String[0]));
            }
        }

        // Convert List<String[]> to String[][]
        return dataList.toArray(new String[0][]);
    }
}
