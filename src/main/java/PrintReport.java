package main.java;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PrintReport {
    public static void printPropertyAssessment(PropertyAssessment assessment) {
        if (assessment == null) {
            System.out.println("Property not found.");
            return;
        }

        String assessmentClasses = Arrays.stream(assessment.getAssessmentClasses())
                .filter(c -> c != null && !c.isEmpty())
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
                assessmentClasses,
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