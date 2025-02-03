package main.java;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        PropertyAssessments assessments = new PropertyAssessments();

        // Get filename and load data
        System.out.print("Enter CSV filename: ");
        String filename = scanner.nextLine();

        try {
            Path filePath = FileUtils.buildFilePath(filename);
            String[][] rawData = ParseCSV.readData(filePath);

            // Process all properties
            for (String[] row : rawData) {
                assessments.addAssessment(new PropertyAssessment(row));
            }

            // Print city-wide statistics
            PrintReport.printStatistics(
                    "Descriptive statistics of all property assessments",
                    assessments.getCityStatistics()
            );

            // Find property by account number
            System.out.print("\nFind a property assessment by account number: ");
            String accountNumber = scanner.nextLine();
            PrintReport.printPropertyAssessment(
                    assessments.findByAccountNumber(accountNumber)
            );

            // Get neighbourhood statistics
            System.out.print("\nNeighbourhood: ");
            String neighbourhoodName = scanner.nextLine();
            Neighbourhood hood = assessments.getNeighbourhood(neighbourhoodName);

            if (hood != null) {
                PrintReport.printStatistics(
                        "Statistics of " + neighbourhoodName + " neighbourhood",
                        hood.getStatistics()
                );
            } else {
                System.out.println("Neighbourhood not found.");
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
