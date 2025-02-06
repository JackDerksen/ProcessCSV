package main.java;

import java.util.List;
import java.util.Collection;

public class Statistics {
    private final int count;
    private final long min;
    private final long max;
    private final long range;
    private final double mean;
    private final long median;

    private Statistics(int count, long min, long max, long range, double mean, long median) {
        this.count = count;
        this.min = min;
        this.max = max;
        this.range = range;
        this.mean = mean;
        this.median = median;
    }

    public static Statistics fromProperties(Collection<PropertyAssessment> properties) {
        if (properties == null || properties.isEmpty()) {
            return new Statistics(0, 0, 0, 0, 0, 0);
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

        return new Statistics(
                values.size(),
                min,
                max,
                max - min,
                mean,
                median
        );
    }

    public static Statistics forHood(Collection<PropertyAssessment> properties) {
        if (properties == null || properties.isEmpty()) {
            return new Statistics(0, 0, 0, 0, 0, 0);
        }

        List<Long> values = properties.stream()
                .mapToLong(PropertyAssessment::getAssessedValue)
                .boxed()
                .sorted()
                .toList();

        double mean = values.stream().mapToLong(v -> v).average().orElse(0.0);
        long median = calculateMedian(values);

        return new Statistics(
                values.size(),
                0,
                0,
                0,
                mean,
                median
        );
    }

    private static long calculateMedian(List<Long> sorted) {
        int size = sorted.size();
        if (size % 2 == 0) {
            return (sorted.get(size / 2) + sorted.get(size / 2 - 1)) / 2;
        } else {
            return sorted.get(size / 2);
        }
    }

    // Getters
    public int getCount() { return count; }
    public long getMin() { return min; }
    public long getMax() { return max; }
    public long getRange() { return range; }
    public double getMean() { return mean; }
    public long getMedian() { return median; }

    @Override
    public String toString() {
        return String.format("""
            n: %d
            Min: $%,d
            Max: $%,d
            Range: $%,d
            Mean: $%,.2f
            Median: $%,d""",
                count, min, max, range, mean, median);
    }
}