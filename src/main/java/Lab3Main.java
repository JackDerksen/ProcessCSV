import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;

public class Lab3Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        PropertyAssessments assessments = new PropertyAssessments();

        // Get filename and load data
        System.out.print("Enter CSV filename: ");
        String filename = scanner.nextLine();

        try {
            // Load and process data
            Path filePath = FileUtils.buildFilePath(filename);
            String[][] rawData = ParseCSV.readData(filePath);

            for (String[] row : rawData) {
                assessments.addAssessment(new PropertyAssessment(row));
            }

            // Neighbourhood analysis
            System.out.print("\nPlease enter a neighbourhood name: ");
            String hoodName = scanner.nextLine();

            Neighbourhood hood = assessments.getNeighbourhood(hoodName);
            if (hood != null) {
                // Get simple statistics for neighbourhood
                Map<String, Object> hoodStats = hood.getStatistics();
                PrintReport.printSimpleHoodStats(hoodName, hoodStats);
            } else {
                System.out.println("Sorry, can't find data for " + hoodName);
            }

            // Assessment class analysis
            System.out.print("\nPlease enter an assessment class: ");
            String className = scanner.nextLine();

            Map<String, Object> classStats = assessments.getAssessmentClassStatistics(className);
            PrintReport.printSimpleClassStats(className, classStats);

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}