import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Get filename and load data
        System.out.print("Enter CSV filename: ");
        String filename = scanner.nextLine();

        try {
            Path filePath = FileUtils.buildFilePath(filename);
            String[][] rawData = ParseCSV.readData(filePath);

            if (!rawData[0][0].isEmpty()) {
                System.out.println("Successfully read file");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
