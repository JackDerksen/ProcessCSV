package main.java;

import java.util.*;

public class PropertyAssessments {
    private final List<PropertyAssessment> assessments;
    private final Map<String, Neighbourhood> neighbourhoods;

    public PropertyAssessments() {
        this.assessments = new ArrayList<>();
        this.neighbourhoods = new HashMap<>();
    }

    public void addAssessment(PropertyAssessment assessment) {
        assessments.add(assessment);
        neighbourhoods
                .computeIfAbsent(assessment.getNeighbourhood(), Neighbourhood::new)
                .addProperty(assessment);
    }

    public PropertyAssessment findByAccountNumber(String accountNumber) {
        return assessments.stream()
                .filter(p -> p.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }

    public Neighbourhood getNeighbourhood(String name) {
        return neighbourhoods.get(name);
    }

    public Statistics getCityStatistics() {
        if (assessments.isEmpty()) {
            return new Statistics(0, 0, 0, 0, 0, 0);
        }

        // Get a sorted list of assessment values to analyze
        List<Long> values = assessments.stream()
                .mapToLong(PropertyAssessment::getAssessedValue)
                .boxed()
                .sorted()
                .toList();

        long min = values.getFirst();
        long max = values.getLast();
        double mean = values.stream().mapToLong(v -> v).average().orElse(0.0);
        long median = calculateMedian(values);

        return new Statistics(
                assessments.size(),
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
