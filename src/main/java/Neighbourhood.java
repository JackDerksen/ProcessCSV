package main.java;

import java.util.ArrayList;
import java.util.List;

public class Neighbourhood {
    private final String name;
    private final List<PropertyAssessment> properties;

    public Neighbourhood(String name) {
        this.name = name;
        this.properties = new ArrayList<>();
    }

    public void addProperty(PropertyAssessment property) {
        properties.add(property);
    }

    public String getName() { return name; }

    public int getCount() { return properties.size(); }

    public Statistics getStatistics() {
        if (properties.isEmpty()) {
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
                properties.size(),
                (int) min,
                max,
                max - min,
                mean,
                median
        );
    }

    private long calculateMedian(List<Long> sorted) {
        int size = sorted.size();
        if (size % 2 == 0) {
            return (sorted.get(size / 2) + sorted.get(size / 2 - 1)) / 2;
        } else {
            return sorted.get(size / 2);
        }
    }
}
