import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CalculateStatisticsTest {

    private List<PropertyAssessment> properties;

    @BeforeEach
    public void setUp() {
        properties = new ArrayList<>();

        // Add properties with various assessment classes and values
        properties.add(createProperty("1001", 300000, "Residential", 100.0));
        properties.add(createProperty("1002", 500000, "Residential", 100.0));
        properties.add(createProperty("1003", 700000, "Residential", 100.0));
        properties.add(createProperty("1004", 250000, "Commercial", 100.0));
        properties.add(createProperty("1005", 450000, "Commercial", 100.0));
        properties.add(createProperty("1006", 1000000, "Industrial", 100.0));
        properties.add(createProperty("1007", 350000, "Residential", 80.0, "Commercial", 20.0));
        properties.add(createProperty("1008", 600000, "Commercial", 60.0, "Industrial", 40.0));
    }

    @Test
    @DisplayName("Test calculateAssessmentClassStats with existing class")
    public void testCalculateAssessmentClassStatsWithExistingClass() {
        // Act
        Map<String, Object> stats = CalculateStatistics.calculateAssessmentClassStats(properties, "Residential");

        // Assert
        assertEquals(4, stats.get("count"));

        // The mean should include properties 1001, 1002, 1003, 1007
        // Values: 300000, 500000, 700000, 350000 = 1,850,000 / 4 = 462,500
        assertEquals(462500.0, (Double)stats.get("mean"), 0.01);

        // The median of [300000, 350000, 500000, 700000] = (350000 + 500000) / 2 = 425000
        assertEquals(425000L, (Long)stats.get("median"));
    }

    @Test
    @DisplayName("Test calculateAssessmentClassStats with case insensitivity")
    public void testCalculateAssessmentClassStatsCaseInsensitive() {
        // Act
        Map<String, Object> stats = CalculateStatistics.calculateAssessmentClassStats(properties, "RESIDENTIAL");

        // Assert
        assertEquals(4, stats.get("count"));
    }

    @Test
    @DisplayName("Test calculateAssessmentClassStats with non-existent class")
    public void testCalculateAssessmentClassStatsWithNonExistentClass() {
        // Act
        Map<String, Object> stats = CalculateStatistics.calculateAssessmentClassStats(properties, "Farm");

        // Assert
        assertEquals(0, stats.get("count"));
        assertEquals(0.0, (Double)stats.get("mean"), 0.01);
        assertEquals(0L, (Long)stats.get("median"));
    }

    @Test
    @DisplayName("Test calculateAssessmentClassStats with empty collection")
    public void testCalculateAssessmentClassStatsWithEmptyCollection() {
        // Act
        Map<String, Object> stats = CalculateStatistics.calculateAssessmentClassStats(new ArrayList<>(), "Residential");

        // Assert
        assertEquals(0, stats.get("count"));
        assertEquals(0.0, (Double)stats.get("mean"), 0.01);
        assertEquals(0L, (Long)stats.get("median"));
    }

    @Test
    @DisplayName("Test calculateAllStats with valid properties")
    public void testCalculateAllStatsWithValidProperties() {
        // Act
        Map<String, Object> stats = CalculateStatistics.calculateAllStats(properties);

        // Assert
        assertEquals(8, stats.get("count"));

        // Min value is 250000 (property 1004)
        assertEquals(250000L, (Long)stats.get("min"));

        // Max value is 1000000 (property 1006)
        assertEquals(1000000L, (Long)stats.get("max"));

        // Range is max - min = 1000000 - 250000 = 750000
        assertEquals(750000L, (Long)stats.get("range"));

        // Mean of all values: (300000 + 500000 + 700000 + 250000 + 450000 + 1000000 + 350000 + 600000) / 8 = 4150000 / 8 = 518750
        assertEquals(518750.0, (Double)stats.get("mean"), 0.01);

        // Median of [250000, 300000, 350000, 450000, 500000, 600000, 700000, 1000000] = (450000 + 500000) / 2 = 475000
        assertEquals(475000L, (Long)stats.get("median"));
    }

    @Test
    @DisplayName("Test calculateAllStats with empty collection")
    public void testCalculateAllStatsWithEmptyCollection() {
        // Act
        Map<String, Object> stats = CalculateStatistics.calculateAllStats(new ArrayList<>());

        // Assert
        assertEquals(0, stats.get("count"));
        assertEquals(0L, (Long)stats.get("min"));
        assertEquals(0L, (Long)stats.get("max"));
        assertEquals(0L, (Long)stats.get("range"));
        assertEquals(0.0, (Double)stats.get("mean"), 0.01);
        assertEquals(0L, (Long)stats.get("median"));
    }

    @Test
    @DisplayName("Test calculateAllStats with null collection")
    public void testCalculateAllStatsWithNullCollection() {
        // Act
        Map<String, Object> stats = CalculateStatistics.calculateAllStats(null);

        // Assert
        assertEquals(0, stats.get("count"));
        assertEquals(0L, (Long)stats.get("min"));
        assertEquals(0L, (Long)stats.get("max"));
        assertEquals(0L, (Long)stats.get("range"));
        assertEquals(0.0, (Double)stats.get("mean"), 0.01);
        assertEquals(0L, (Long)stats.get("median"));
    }

    @Test
    @DisplayName("Test calculateNeighbourhoodStats with valid properties")
    public void testCalculateNeighbourhoodStatsWithValidProperties() {
        // Arrange - Create a new list with properties in the same neighbourhood
        List<PropertyAssessment> neighbourhoodProperties = new ArrayList<>();
        neighbourhoodProperties.add(createProperty("1001", 300000, "Residential", 100.0, "Downtown"));
        neighbourhoodProperties.add(createProperty("1002", 500000, "Residential", 100.0, "Downtown"));
        neighbourhoodProperties.add(createProperty("1003", 700000, "Residential", 100.0, "Downtown"));

        // Act
        Map<String, Object> stats = CalculateStatistics.calculateNeighbourhoodStats(neighbourhoodProperties);

        // Assert
        assertEquals(3, stats.get("count"));

        // Mean: (300000 + 500000 + 700000) / 3 = 1500000 / 3 = 500000
        assertEquals(500000.0, (Double)stats.get("mean"), 0.01);

        // Median of [300000, 500000, 700000] = 500000
        assertEquals(500000L, (Long)stats.get("median"));
    }

    @Test
    @DisplayName("Test calculateNeighbourhoodStats with empty collection")
    public void testCalculateNeighbourhoodStatsWithEmptyCollection() {
        // Act
        Map<String, Object> stats = CalculateStatistics.calculateNeighbourhoodStats(new ArrayList<>());

        // Assert
        assertEquals(0, stats.get("count"));
        assertEquals(0.0, (Double)stats.get("mean"), 0.01);
        assertEquals(0L, (Long)stats.get("median"));
    }

    @Test
    @DisplayName("Test calculateNeighbourhoodStats with null collection")
    public void testCalculateNeighbourhoodStatsWithNullCollection() {
        // Act
        Map<String, Object> stats = CalculateStatistics.calculateNeighbourhoodStats(null);

        // Assert
        assertEquals(0, stats.get("count"));
        assertEquals(0.0, (Double)stats.get("mean"), 0.01);
        assertEquals(0L, (Long)stats.get("median"));
    }

    @Test
    @DisplayName("Test median calculation with odd number of values")
    public void testMedianWithOddNumberOfValues() {
        // Arrange
        List<PropertyAssessment> oddProperties = Arrays.asList(
                createProperty("1001", 300000, "Residential", 100.0),
                createProperty("1002", 500000, "Residential", 100.0),
                createProperty("1003", 700000, "Residential", 100.0)
        );

        // Act
        Map<String, Object> stats = CalculateStatistics.calculateNeighbourhoodStats(oddProperties);

        // Assert - Median of [300000, 500000, 700000] = 500000
        assertEquals(500000L, (Long)stats.get("median"));
    }

    @Test
    @DisplayName("Test median calculation with even number of values")
    public void testMedianWithEvenNumberOfValues() {
        // Arrange
        List<PropertyAssessment> evenProperties = Arrays.asList(
                createProperty("1001", 300000, "Residential", 100.0),
                createProperty("1002", 500000, "Residential", 100.0),
                createProperty("1003", 700000, "Residential", 100.0),
                createProperty("1004", 900000, "Residential", 100.0)
        );

        // Act
        Map<String, Object> stats = CalculateStatistics.calculateNeighbourhoodStats(evenProperties);

        // Assert - Median of [300000, 500000, 700000, 900000] = (500000 + 700000) / 2 = 600000
        assertEquals(600000L, (Long)stats.get("median"));
    }

    @Test
    @DisplayName("Test median calculation with single value")
    public void testMedianWithSingleValue() {
        // Arrange
        List<PropertyAssessment> singleProperty = List.of(
                createProperty("1001", 300000, "Residential", 100.0)
        );

        // Act
        Map<String, Object> stats = CalculateStatistics.calculateNeighbourhoodStats(singleProperty);

        // Assert - Median of [300000] = 300000
        assertEquals(300000L, (Long)stats.get("median"));
    }

    // Helper methods
    private PropertyAssessment createProperty(String accountNumber, long assessedValue,
                                              String className, double percentage) {
        return createProperty(accountNumber, assessedValue, className, percentage, null, 0.0, "Downtown");
    }

    private PropertyAssessment createProperty(String accountNumber, long assessedValue,
                                              String className1, double percentage1,
                                              String className2, double percentage2) {
        return createProperty(accountNumber, assessedValue, className1, percentage1, className2, percentage2, "Downtown");
    }

    private PropertyAssessment createProperty(String accountNumber, long assessedValue,
                                              String className, double percentage, String neighbourhood) {
        return createProperty(accountNumber, assessedValue, className, percentage, null, 0.0, neighbourhood);
    }

    private PropertyAssessment createProperty(String accountNumber, long assessedValue,
                                              String className1, double percentage1,
                                              String className2, double percentage2,
                                              String neighbourhood) {
        String[] classes = {className1, className2 != null ? className2 : "", ""};
        double[] percentages = {percentage1, className2 != null ? percentage2 : 0.0, 0.0};

        return new PropertyAssessment(
                accountNumber,
                new Address("", "123", "Test St"),
                neighbourhood,
                assessedValue,
                new Location(0, 0),
                classes,
                percentages,
                "Ward 1"
        );
    }
}