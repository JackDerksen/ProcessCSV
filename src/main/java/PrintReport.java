package main.java;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PrintReport {
    public static void printPropertyAssessment(PropertyAssessment assessment) {
        if (assessment == null) {
            System.out.println("Property not found.");
            return;
        }

        String assessmentClassesWithPercentages = Arrays.stream(assessment.getAssessmentClasses())
                .filter(c -> c != null && !c.isEmpty())
                .map(className -> {
                    double percentage = assessment.getAssessmentClassPercentage(className);
                    return String.format("%s (%.1f%%)", className, percentage);
                })
                .collect(Collectors.joining(", "));

        System.out.printf("""
            Assessment of property %s
            -----------------------------------------------------
            Address: %s
            Assessed value: $%,d
            Assessment class: %s
            Neighbourhood: %s
            Location: %s
            %n""",
                assessment.getAccountNumber(),
                assessment.getAddress(),
                assessment.getAssessedValue(),
                assessmentClassesWithPercentages,
                assessment.getNeighbourhood(),
                assessment.getLocation()
        );
    }

    public static void printStatistics(String title, Statistics stats) {
        System.out.printf("""
            %s
            %s
            %s
            %n""",
                title,
                "-".repeat(title.length()),
                stats.toString()
        );
    }

    public static void printHoodStats(String hoodName, Neighbourhood hood) {
        if (hood != null) {
            // Get statistics using your existing method which calculates count, min, max, range, mean and median.
            Statistics hoodStats = hood.getStatistics();
            System.out.println("There are " + hoodStats.getCount() + " properties in " + hoodName);
            System.out.printf("The mean value is $%,.2f%n", hoodStats.getMean());
            System.out.printf("The median value is $%,d%n", hoodStats.getMedian());
        } else {
            System.out.println("Sorry, can't find data for " + hoodName);
        }
    }

    public static void printClassStats(String className, Statistics classStats) {
        if (classStats.getCount() > 0) {
            // Only print count, min, max, and range
            System.out.println("There are " + classStats.getCount() + " " + className + " properties in Edmonton");
            System.out.printf("The min value is $%,d%n", classStats.getMin());
            System.out.printf("The max value is $%,d%n", classStats.getMax());
            System.out.printf("The range is $%,d%n", classStats.getRange());
        } else {
            System.out.println("Sorry, no properties found for assessment class: " + className);
        }
    }
}