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

    // Print neighborhood statistics (removed duplicate method)
    public static void printHoodStats(String hoodName, Map<String, Object> stats) {
        if (stats.get("count") != null && ((Number)stats.get("count")).intValue() > 0) {
            System.out.println("There are " + ((Number)stats.get("count")).intValue() + " properties in " + hoodName);
            System.out.printf("The mean value is $%,.2f%n", ((Number)stats.get("mean")).doubleValue());
            System.out.printf("The median value is $%,d%n", ((Number)stats.get("median")).longValue());
        } else {
            System.out.println("Sorry, can't find data for " + hoodName);
        }
    }

    // Print assessment class statistics with different formats
    public static void printClassStats(String className, Map<String, Object> stats, boolean includeMinMaxRange) {
        if (stats.get("count") != null && ((Number)stats.get("count")).intValue() > 0) {
            System.out.println("There are " + ((Number)stats.get("count")).intValue() + " " + className + " properties in Edmonton");

            // If detailed stats are requested, include min, max, and range
            if (includeMinMaxRange && stats.containsKey("min") && stats.containsKey("max") && stats.containsKey("range")) {
                System.out.printf("The min value is $%,d%n", ((Number)stats.get("min")).longValue());
                System.out.printf("The max value is $%,d%n", ((Number)stats.get("max")).longValue());
                System.out.printf("The range is $%,d%n", ((Number)stats.get("range")).longValue());
            } else if (stats.containsKey("mean") && stats.containsKey("median")) {
                // Otherwise just show mean and median
                System.out.printf("The mean value is $%,.2f%n", ((Number)stats.get("mean")).doubleValue());
                System.out.printf("The median value is $%,d%n", ((Number)stats.get("median")).longValue());
            }
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
                ((Number)stats.get("count")).intValue(),
                ((Number)stats.get("min")).longValue(),
                ((Number)stats.get("max")).longValue(),
                ((Number)stats.get("range")).longValue(),
                ((Number)stats.get("mean")).doubleValue(),
                ((Number)stats.get("median")).longValue()
        );
    }
}