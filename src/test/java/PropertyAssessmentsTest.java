import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyAssessmentsTest {

    private PropertyAssessments assessments;
    private PropertyAssessment downtownProperty1;
    private PropertyAssessment downtownProperty2;
    private PropertyAssessment uptownProperty;
    private PropertyAssessment mixedUseProperty;

    @BeforeEach
    public void setUp() {
        // Create a new PropertyAssessments object
        assessments = new PropertyAssessments();

        // Create test properties for different neighbourhoods and assessment classes
        downtownProperty1 = createProperty("1001", "Downtown", 300000, "Residential", 100.0);
        downtownProperty2 = createProperty("1002", "Downtown", 500000, "Residential", 100.0);
        uptownProperty = createProperty("1003", "Uptown", 700000, "Commercial", 100.0);
        mixedUseProperty = createProperty("1004", "Midtown", 800000, "Residential", 60.0, "Commercial", 40.0);
    }

    @Test
    @DisplayName("Constructor should initialize empty collections")
    public void testConstructor() {
        // Act & Assert
        assertTrue(assessments.getAssessments().isEmpty());
    }

    @Test
    @DisplayName("addAssessment should add a property assessment")
    public void testAddAssessment() {
        // Act
        assessments.addAssessment(downtownProperty1);

        // Assert
        assertEquals(1, assessments.getAssessments().size());
        assertTrue(assessments.getAssessments().contains(downtownProperty1));
    }

    @Test
    @DisplayName("addAssessment should organize properties by neighbourhood")
    public void testAddAssessmentOrganizesNeighbourhoods() {
        // Act
        assessments.addAssessment(downtownProperty1);
        assessments.addAssessment(downtownProperty2);
        assessments.addAssessment(uptownProperty);

        // Assert
        Neighbourhood downtown = assessments.getNeighbourhood("Downtown");
        Neighbourhood uptown = assessments.getNeighbourhood("Uptown");

        assertNotNull(downtown);
        assertNotNull(uptown);
        assertEquals(2, downtown.getCount());
        assertEquals(1, uptown.getCount());

        assertTrue(downtown.getProperties().contains(downtownProperty1));
        assertTrue(downtown.getProperties().contains(downtownProperty2));
        assertTrue(uptown.getProperties().contains(uptownProperty));
    }

    @Test
    @DisplayName("getAssessments should return a copy of the assessments list")
    public void testGetAssessmentsReturnsCopy() {
        // Arrange
        assessments.addAssessment(downtownProperty1);
        assessments.addAssessment(downtownProperty2);

        // Act
        var assessmentsList = assessments.getAssessments();
        assessmentsList.add(uptownProperty); // Modify the copy

        // Assert
        assertEquals(2, assessments.getAssessments().size()); // Original should be unchanged
        assertEquals(3, assessmentsList.size()); // Copy should have the added property
    }

    @Test
    @DisplayName("findByAccountNumber should return property with matching account number")
    public void testFindByAccountNumber() {
        // Arrange
        assessments.addAssessment(downtownProperty1);
        assessments.addAssessment(downtownProperty2);
        assessments.addAssessment(uptownProperty);

        // Act
        PropertyAssessment found = assessments.findByAccountNumber("1002");

        // Assert
        assertNotNull(found);
        assertEquals(downtownProperty2, found);
    }

    @Test
    @DisplayName("findByAccountNumber should return null for non-existent account number")
    public void testFindByAccountNumberWithNonExistent() {
        // Arrange
        assessments.addAssessment(downtownProperty1);
        assessments.addAssessment(downtownProperty2);

        // Act
        PropertyAssessment found = assessments.findByAccountNumber("9999");

        // Assert
        assertNull(found);
    }

    @Test
    @DisplayName("getNeighbourhood should find neighbourhood with case-insensitive name")
    public void testGetNeighbourhoodCaseInsensitive() {
        // Arrange
        assessments.addAssessment(downtownProperty1);

        // Act
        Neighbourhood downtown1 = assessments.getNeighbourhood("Downtown");
        Neighbourhood downtown2 = assessments.getNeighbourhood("downtown");
        Neighbourhood downtown3 = assessments.getNeighbourhood("DOWNTOWN");

        // Assert
        assertNotNull(downtown1);
        assertNotNull(downtown2);
        assertNotNull(downtown3);
        assertEquals(downtown1, downtown2);
        assertEquals(downtown1, downtown3);
    }

    @Test
    @DisplayName("getNeighbourhood should return null for non-existent neighbourhood")
    public void testGetNeighbourhoodWithNonExistent() {
        // Arrange
        assessments.addAssessment(downtownProperty1);

        // Act
        Neighbourhood nonExistent = assessments.getNeighbourhood("NonExistentNeighbourhood");

        // Assert
        assertNull(nonExistent);
    }

    @Test
    @DisplayName("getNeighbourhood should return null when passed null")
    public void testGetNeighbourhoodWithNull() {
        // Arrange
        assessments.addAssessment(downtownProperty1);

        // Act
        Neighbourhood result = assessments.getNeighbourhood(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("getCityStatistics should return statistics for all properties")
    public void testGetCityStatistics() {
        // Arrange
        assessments.addAssessment(downtownProperty1); // 300000
        assessments.addAssessment(downtownProperty2); // 500000
        assessments.addAssessment(uptownProperty);    // 700000

        // Act
        Map<String, Object> stats = assessments.getCityStatistics();

        // Assert
        assertEquals(3, stats.get("count"));
        assertEquals(300000L, stats.get("min"));
        assertEquals(700000L, stats.get("max"));
        assertEquals(400000L, stats.get("range"));
        assertEquals(500000.0, (Double)stats.get("mean"), 0.01);
        assertEquals(500000L, (Long)stats.get("median"));
    }

    @Test
    @DisplayName("getCityStatistics should handle empty assessments")
    public void testGetCityStatisticsWithEmpty() {
        // Act
        Map<String, Object> stats = assessments.getCityStatistics();

        // Assert
        assertEquals(0, stats.get("count"));
        assertEquals(0L, stats.get("min"));
        assertEquals(0L, stats.get("max"));
        assertEquals(0L, stats.get("range"));
        assertEquals(0.0, (Double)stats.get("mean"), 0.01);
        assertEquals(0L, (Long)stats.get("median"));
    }

    @Test
    @DisplayName("getAssessmentClassStatistics should return statistics for specific class")
    public void testGetAssessmentClassStatistics() {
        // Arrange
        assessments.addAssessment(downtownProperty1); // Residential 300000
        assessments.addAssessment(downtownProperty2); // Residential 500000
        assessments.addAssessment(uptownProperty);    // Commercial 700000
        assessments.addAssessment(mixedUseProperty);  // 60% Residential, 40% Commercial 800000

        // Act
        Map<String, Object> residentialStats = assessments.getAssessmentClassStatistics("Residential");
        Map<String, Object> commercialStats = assessments.getAssessmentClassStatistics("Commercial");

        // Assert - Residential
        assertEquals(3, residentialStats.get("count")); // 3 properties with Residential
        // Mean of [300000, 500000, 800000] = 533333.33
        assertEquals(533333.33, (Double)residentialStats.get("mean"), 0.01);
        // Median of [300000, 500000, 800000] = 500000
        assertEquals(500000L, (Long)residentialStats.get("median"));

        // Assert - Commercial
        assertEquals(2, commercialStats.get("count")); // 2 properties with Commercial
        // Mean of [700000, 800000] = 750000
        assertEquals(750000.0, (Double)commercialStats.get("mean"), 0.01);
        // Median of [700000, 800000] = 750000
        assertEquals(750000L, (Long)commercialStats.get("median"));
    }

    @Test
    @DisplayName("getAssessmentClassStatistics should be case insensitive")
    public void testGetAssessmentClassStatisticsCaseInsensitive() {
        // Arrange
        assessments.addAssessment(downtownProperty1); // Residential 300000
        assessments.addAssessment(downtownProperty2); // Residential 500000

        // Act
        Map<String, Object> stats1 = assessments.getAssessmentClassStatistics("Residential");
        Map<String, Object> stats2 = assessments.getAssessmentClassStatistics("residential");
        Map<String, Object> stats3 = assessments.getAssessmentClassStatistics("RESIDENTIAL");

        // Assert
        assertEquals(2, stats1.get("count"));
        assertEquals(2, stats2.get("count"));
        assertEquals(2, stats3.get("count"));
        assertEquals((Double)stats1.get("mean"), (Double)stats2.get("mean"), 0.01);
        assertEquals((Double)stats1.get("mean"), (Double)stats3.get("mean"), 0.01);
    }

    @Test
    @DisplayName("getAssessmentClassStatistics should handle non-existent class")
    public void testGetAssessmentClassStatisticsWithNonExistent() {
        // Arrange
        assessments.addAssessment(downtownProperty1);
        assessments.addAssessment(downtownProperty2);

        // Act
        Map<String, Object> stats = assessments.getAssessmentClassStatistics("Farm");

        // Assert
        assertEquals(0, stats.get("count"));
        assertEquals(0.0, (Double)stats.get("mean"), 0.01);
        assertEquals(0L, (Long)stats.get("median"));
    }

    // Helper method
    private PropertyAssessment createProperty(String accountNumber, String neighbourhood,
                                              long assessedValue, String className, double percentage) {
        return createProperty(accountNumber, neighbourhood, assessedValue, className, percentage, null, 0.0);
    }

    private PropertyAssessment createProperty(String accountNumber, String neighbourhood,
                                              long assessedValue, String className1, double percentage1,
                                              String className2, double percentage2) {
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