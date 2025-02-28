import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;

public class InfographicMain {
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

            InfographicData infographicData = new InfographicData(assessments);

            // 1. Property Type Distribution
            System.out.println("\n===== PROPERTY TYPE DISTRIBUTION =====");
            Map<String, Integer> distribution = infographicData.getPropertyTypeDistribution();
            for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
                System.out.printf("%s: %d properties (%.1f%%)\n",
                        entry.getKey(),
                        entry.getValue(),
                        (double) entry.getValue() / assessments.getAssessments().size() * 100);
            }

            // 2. Average Values by Property Type
            System.out.println("\n===== AVERAGE VALUES BY PROPERTY TYPE =====");
            Map<String, Double> avgValues = infographicData.getAverageValueByPropertyType();
            for (Map.Entry<String, Double> entry : avgValues.entrySet()) {
                System.out.printf("%s: $%,.2f\n", entry.getKey(), entry.getValue());
            }

            // 3. Ward Analysis
            System.out.println("\n===== TOP 5 HIGHEST VALUED WARDS =====");
            Map<String, Double> highestWards = infographicData.getHighestValuedWards(5);
            for (Map.Entry<String, Double> entry : highestWards.entrySet()) {
                System.out.printf("%s: $%,.2f\n", entry.getKey(), entry.getValue());
            }

            System.out.println("\n===== TOP 5 LOWEST VALUED WARDS =====");
            Map<String, Double> lowestWards = infographicData.getLowestValuedWards(5);
            for (Map.Entry<String, Double> entry : lowestWards.entrySet()) {
                System.out.printf("%s: $%,.2f\n", entry.getKey(), entry.getValue());
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
