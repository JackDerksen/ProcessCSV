package main.java;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<PropertyAssessment> getAssessments() {
        return assessments;
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
        return Statistics.fromProperties(assessments);
    }

    public Statistics getAssessmentClassStatistics(String assessmentClass) {
        // Filter properties where the percentage for this class is > 0.
        List<PropertyAssessment> filteredProperties = assessments.stream()
                .filter(p -> p.getAssessmentClassPercentage(assessmentClass) > 0)
                .collect(Collectors.toList());
        return Statistics.fromProperties(filteredProperties);
    }
}