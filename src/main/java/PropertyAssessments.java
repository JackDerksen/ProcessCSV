import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Collection class for managing multiple property assessments, with methods
 * for searching, grouping by neighborhood, and calculating statistics.
 */

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
        return new ArrayList<>(assessments); // Return a copy to prevent modification
    }

    public PropertyAssessment findByAccountNumber(String accountNumber) {
        return assessments.stream()
                .filter(p -> p.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }

    public Neighbourhood getNeighbourhood(String name) {
        if (name == null) return null;
        return neighbourhoods.entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(name))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    public Map<String, Object> getCityStatistics() {
        return CalculateStatistics.calculateAllStats(assessments);
    }

    public Map<String, Object> getAssessmentClassStatistics(String assessmentClass) {
        return CalculateStatistics.calculateAssessmentClassStats(assessments, assessmentClass);
    }
}