import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;

public class Lab2Main {
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

            // 1. Print statistics for all properties in the CSV file
            Map<String, Object> cityStats = CalculateStatistics.calculateAllStats(assessments.getAssessments());
            System.out.println("\n===== CITY-WIDE PROPERTY STATISTICS =====");
            PrintReport.printAllStats("Edmonton Property Assessments", cityStats);

            // 2. Get a specific property by account number
            System.out.print("\nPlease enter an account number: ");
            String accountNumber = scanner.nextLine();
            PropertyAssessment property = assessments.findByAccountNumber(accountNumber);

            if (property != null) {
                System.out.println("\n===== PROPERTY DETAILS =====");
                PrintReport.printPropertyAssessment(property);
            } else {
                System.out.println("Property not found with account number: " + accountNumber);
            }

            // 3. Get statistics for a specific neighbourhood
            System.out.print("\nPlease enter a neighbourhood name: ");
            String hoodName = scanner.nextLine();
            Neighbourhood hood = assessments.getNeighbourhood(hoodName);

            if (hood != null) {
                System.out.println("\n===== NEIGHBOURHOOD STATISTICS =====");
                HoodAssessment hoodAssessment = new HoodAssessment(hood);
                hoodAssessment.printSummary();
            } else {
                System.out.println("Neighbourhood not found: " + hoodName);
            }

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