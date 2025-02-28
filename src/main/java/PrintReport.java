import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class PrintReport {
    // Print complete property assessment details
    public static void printPropertyAssessment(PropertyAssessment assessment) {
        if (assessment == null) {
            System.out.println("Property not found.");
            return;
        }

        // Format the assessment classes with their percentages
        String assessmentClassesWithPercentages = Arrays.stream(assessment.getAssessmentClasses())
                .filter(c -> c != null && !c.isEmpty())
                .map(className -> {
                    double percentage = assessment.getAssessmentClassPercentage(className);
                    return String.format("%s (%.1f%%)", className, percentage);
                })
                .collect(Collectors.joining(", "));

        // Print a formatted property report
        System.out.printf("""
            Assessment of property #%s
            -------------------------------
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

    // Print neighborhood statistics
    public static void printHoodStats(String hoodName, Map<String, Object> stats) {
        if (stats.get("count") != null && (Integer)stats.get("count") > 0) {
            System.out.println("There are " + stats.get("count") + " properties in " + hoodName);
            System.out.printf("The mean value is $%,.2f%n", stats.get("mean"));
            System.out.printf("The median value is $%,d%n", stats.get("median"));
        } else {
            System.out.println("Sorry, can't find data for " + hoodName);
        }
    }

    // Print assessment class statistics
    public static void printClassStats(String className, Map<String, Object> stats) {
        if (stats.get("count") != null && (Integer)stats.get("count") > 0) {
            System.out.println("There are " + stats.get("count") + " " + className + " properties in Edmonton");
            System.out.printf("The min value is $%,d%n", stats.get("min"));
            System.out.printf("The max value is $%,d%n", stats.get("max"));
            System.out.printf("The range is $%,d%n", stats.get("range"));
        } else {
            System.out.println("Sorry, no properties found for assessment class: " + className);
        }
    }

    // Print full statistics
    public static void printAllStats(String title, Map<String, Object> stats) {
        System.out.printf("""
            %s
            -------------------------------
            n: %d
            Min: $%,d
            Max: $%,d
            Range: $%,d
            Mean: $%,.2f
            Median: $%,d%n""",
                title,
                stats.get("count"),
                stats.get("min"),
                stats.get("max"),
                stats.get("range"),
                stats.get("mean"),
                stats.get("median")
        );
    }
}