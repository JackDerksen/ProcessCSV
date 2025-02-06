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

            // Neighbourhood Analysis
            System.out.print("Please enter a neighbourhood name: ");
            String hoodName = scanner.nextLine();
            Neighbourhood hood = assessments.getNeighbourhood(hoodName);

            if (hood != null) {
                // Get statistics using your existing method which calculates count, min, max, range, mean and median.
                Statistics hoodStats = hood.getStatistics();
                System.out.println("There are " + hoodStats.getCount() + " properties in " + hoodName);
                System.out.printf("The mean value is $%,.2f%n", hoodStats.getMean());
                System.out.printf("The median value is $%,d%n", hoodStats.getMedian());
            } else {
                System.out.println("Sorry, can't find data for " + hoodName);
            }

            // Assessment Class Analysis
            System.out.print("\nPlease enter an assessment class: ");
            String className = scanner.nextLine();
            Statistics classStats = assessments.getAssessmentClassStatistics(className);

            if (classStats.getCount() > 0) {
                // Only print count, min, max, and range
                System.out.println("There are " + classStats.getCount() + " " + className + " properties in Edmonton");
                System.out.printf("The min value is $%,d%n", classStats.getMin());
                System.out.printf("The max value is $%,d%n", classStats.getMax());
                System.out.printf("The range is $%,d%n", classStats.getRange());
            } else {
                System.out.println("Sorry, no properties found for assessment class: " + className);
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
