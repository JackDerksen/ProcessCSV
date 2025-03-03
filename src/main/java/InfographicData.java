/*

MY RESEARCH QUESTIONS:

- Property Type Distribution: What's the distribution of property types (residential, commercial, other residential) across the city?
- Value by Property Type: How do average property values differ between residential and commercial properties?
- Ward Analysis: Which wards have the highest and lowest average property values?

*/

import java.util.*;
import java.util.stream.Collectors;

public class InfographicData {
    private final PropertyAssessments assessments;

    public InfographicData(PropertyAssessments assessments) {
        this.assessments = assessments;
    }

    // Get the distribution of property types across the city
    public Map<String, Integer> getPropertyTypeDistribution() {
        Map<String, Integer> distribution = new HashMap<>();

        // Track all unique assessment classes
        Set<String> uniqueClasses = new HashSet<>();

        // Gather all assessment classes
        for (PropertyAssessment property : assessments.getAssessments()) {
            for (String className : property.getAssessmentClasses()) {
                if (className != null && !className.isEmpty()) {
                    uniqueClasses.add(className);
                }
            }
        }

        // Count properties for each assessment class
        for (String className : uniqueClasses) {
            int count = 0;
            for (PropertyAssessment property : assessments.getAssessments()) {
                if (property.getAssessmentClassPercentage(className) > 0) {
                    count++;
                }
            }
            distribution.put(className, count);
        }

        return distribution;
    }

    // Get average property values by property type
    public Map<String, Double> getAverageValueByPropertyType() {
        Map<String, Double> averageValues = new HashMap<>();
        Map<String, List<Long>> valuesByType = new HashMap<>();

        // Group property values by assessment class
        for (PropertyAssessment property : assessments.getAssessments()) {
            for (String className : property.getAssessmentClasses()) {
                if (className != null && !className.isEmpty() &&
                        property.getAssessmentClassPercentage(className) > 0) {

                    valuesByType.computeIfAbsent(className, _ -> new ArrayList<>())
                            .add(property.getAssessedValue());
                }
            }
        }

        // Calculate average for each type
        for (Map.Entry<String, List<Long>> entry : valuesByType.entrySet()) {
            double average = entry.getValue().stream()
                    .mapToLong(Long::longValue)
                    .average()
                    .orElse(0.0);
            averageValues.put(entry.getKey(), average);
        }

        return averageValues;
    }

    // Get ward statistics - average property values by ward
    public Map<String, Double> getWardAverageValues() {
        Map<String, List<PropertyAssessment>> propertiesByWard = groupPropertiesByWard();

        // Calculate average value for each ward
        Map<String, Double> wardAverages = new HashMap<>();
        for (Map.Entry<String, List<PropertyAssessment>> entry : propertiesByWard.entrySet()) {
            double average = entry.getValue().stream()
                    .mapToLong(PropertyAssessment::getAssessedValue)
                    .average()
                    .orElse(0.0);
            wardAverages.put(entry.getKey(), average);
        }

        return wardAverages;
    }

    // Get the highest valued wards
    public Map<String, Double> getHighestValuedWards(int limit) {
        return getWardAverageValues().entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, _) -> e1,
                        LinkedHashMap::new
                ));
    }


    // Get the lowest valued wards
    public Map<String, Double> getLowestValuedWards(int limit) {
        return getWardAverageValues().entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, _) -> e1,
                        LinkedHashMap::new
                ));
    }

    // Helper method to group properties by ward
    private Map<String, List<PropertyAssessment>> groupPropertiesByWard() {
        Map<String, List<PropertyAssessment>> propertiesByWard = new HashMap<>();

        for (PropertyAssessment property : assessments.getAssessments()) {
            String ward = getPropertyWard(property);
            if (ward != null && !ward.isEmpty()) {
                propertiesByWard.computeIfAbsent(ward, _ -> new ArrayList<>())
                        .add(property);
            }
        }

        return propertiesByWard;
    }

    // Helper method to extract ward from a property
    private String getPropertyWard(PropertyAssessment property) {
        return property.getWard();
    }
}