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

}