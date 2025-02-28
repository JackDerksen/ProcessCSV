import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CalculateStatistics {
    // Calculate and return all statistics in a map
    public static Map<String, Object> calculateAllStats(Collection<PropertyAssessment> properties) {
        Map<String, Object> stats = new HashMap<>();

        if (properties == null || properties.isEmpty()) {
            stats.put("count", 0);
            stats.put("min", 0L);
            stats.put("max", 0L);
            stats.put("range", 0L);
            stats.put("mean", 0.0);
            stats.put("median", 0L);
            return stats;
        }

        List<Long> values = properties.stream()
                .mapToLong(PropertyAssessment::getAssessedValue)
                .boxed()
                .sorted()
                .toList();

        long min = values.getFirst();
        long max = values.getLast();
        double mean = values.stream().mapToLong(v -> v).average().orElse(0.0);
        long median = calculateMedian(values);

        stats.put("count", values.size());
        stats.put("min", min);
        stats.put("max", max);
        stats.put("range", max - min);
        stats.put("mean", mean);
        stats.put("median", median);

        return stats;
    }

    // Calculate and return only neighborhood statistics (count, mean, median)
    public static Map<String, Object> calculateNeighbourhoodStats(Collection<PropertyAssessment> properties) {
        Map<String, Object> stats = new HashMap<>();

        if (properties == null || properties.isEmpty()) {
            stats.put("count", 0);
            stats.put("mean", 0.0);
            stats.put("median", 0L);
            return stats;
        }

        List<Long> values = properties.stream()
                .mapToLong(PropertyAssessment::getAssessedValue)
                .boxed()
                .sorted()
                .toList();

        stats.put("count", values.size());
        stats.put("mean", values.stream().mapToLong(v -> v).average().orElse(0.0));
        stats.put("median", calculateMedian(values));

        return stats;
    }

    // Calculate and return only assessment class statistics (count, min, max, range)
    public static Map<String, Object> calculateAssessmentClassStats(Collection<PropertyAssessment> allProperties, String className) {
        // Filter properties where the percentage for this class is > 0
        List<PropertyAssessment> properties = allProperties.stream()
                .filter(p -> p.getAssessmentClassPercentage(className) > 0)
                .toList();

        Map<String, Object> stats = new HashMap<>();

        if (properties.isEmpty()) {
            stats.put("count", 0);
            stats.put("min", 0L);
            stats.put("max", 0L);
            stats.put("range", 0L);
            return stats;
        }

        List<Long> values = properties.stream()
                .mapToLong(PropertyAssessment::getAssessedValue)
                .boxed()
                .sorted()
                .toList();

        long min = values.getFirst();
        long max = values.getLast();

        stats.put("count", values.size());
        stats.put("min", min);
        stats.put("max", max);
        stats.put("range", max - min);

        return stats;
    }

    // Helper method to calculate median
    private static long calculateMedian(List<Long> sorted) {
        int size = sorted.size();
        if (size % 2 == 0) {
            return (sorted.get(size / 2) + sorted.get(size / 2 - 1)) / 2;
        } else {
            return sorted.get(size / 2);
        }
    }
}