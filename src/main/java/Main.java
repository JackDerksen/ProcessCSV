package main.java;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

import static main.java.PrintReport.printHoodStats;
import static main.java.PrintReport.printClassStats;

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

            // Neighbourhood analysis
            System.out.print("Please enter a neighbourhood name: ");
            String hoodName = scanner.nextLine();
            Neighbourhood hood = assessments.getNeighbourhood(hoodName);

            printHoodStats(hoodName, hood);

            // Assessment class analysis
            System.out.print("\nPlease enter an assessment class: ");
            String className = scanner.nextLine();
            Statistics classStats = assessments.getAssessmentClassStatistics(className);

            printClassStats(className, classStats);

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
