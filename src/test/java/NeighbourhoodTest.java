import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class NeighbourhoodTest {

    private Neighbourhood neighbourhood;
    private PropertyAssessment property1;
    private PropertyAssessment property2;
    private PropertyAssessment property3;

    @BeforeEach
    public void setUp() {
        // Create a test neighbourhood
        neighbourhood = new Neighbourhood("Downtown");

        // Create test properties
        property1 = createProperty("1001", 300000);
        property2 = createProperty("1002", 500000);
        property3 = createProperty("1003", 700000);
    }

    @Test
    @DisplayName("Constructor should set name correctly")
    public void testConstructor() {
        // Act & Assert
        assertEquals("Downtown", neighbourhood.getName());
        assertEquals(0, neighbourhood.getCount());
        assertTrue(neighbourhood.getProperties().isEmpty());
    }

    @Test
    @DisplayName("addProperty should add properties to the neighbourhood")
    public void testAddProperty() {
        // Act
        neighbourhood.addProperty(property1);
        neighbourhood.addProperty(property2);

        // Assert
        assertEquals(2, neighbourhood.getCount());
        assertEquals(2, neighbourhood.getProperties().size());
        assertTrue(neighbourhood.getProperties().contains(property1));
        assertTrue(neighbourhood.getProperties().contains(property2));
    }

    @Test
    @DisplayName("getProperties should return a copy of the properties list")
    public void testGetPropertiesReturnsCopy() {
        // Arrange
        neighbourhood.addProperty(property1);
        neighbourhood.addProperty(property2);

        // Act
        List<PropertyAssessment> properties = neighbourhood.getProperties();
        properties.add(property3); // This should not affect the original list

        // Assert
        assertEquals(2, neighbourhood.getCount());
        assertEquals(3, properties.size()); // The copied list should have 3 properties
    }

    @Test
    @DisplayName("getStatistics should return correct neighbourhood statistics")
    public void testGetStatistics() {
        // Arrange
        neighbourhood.addProperty(property1); // 300000
        neighbourhood.addProperty(property2); // 500000
        neighbourhood.addProperty(property3); // 700000

        // Act
        Map<String, Object> stats = neighbourhood.getStatistics();

        // Assert
        assertEquals(3, stats.get("count"));

        // Mean: (300000 + 500000 + 700000) / 3 = 500000
        assertEquals(500000.0, (Double)stats.get("mean"), 0.01);

        // Median of [300000, 500000, 700000] = 500000
        assertEquals(500000L, (Long)stats.get("median"));
    }

    @Test
    @DisplayName("getStatistics should handle empty neighbourhood")
    public void testGetStatisticsWithEmptyNeighbourhood() {
        // Act
        Map<String, Object> stats = neighbourhood.getStatistics();

        // Assert
        assertEquals(0, stats.get("count"));
        assertEquals(0.0, (Double)stats.get("mean"), 0.01);
        assertEquals(0L, (Long)stats.get("median"));
    }

    @Test
    @DisplayName("toString should format neighbourhood information correctly")
    public void testToString() {
        // Arrange
        neighbourhood.addProperty(property1);
        neighbourhood.addProperty(property2);

        // Act
        String result = neighbourhood.toString();

        // Assert
        assertEquals("Downtown (2 properties)", result);
    }

    @Test
    @DisplayName("equals should return true for neighbourhoods with the same name")
    public void testEqualsWithSameName() {
        // Arrange
        Neighbourhood otherNeighbourhood = new Neighbourhood("Downtown");

        // Act & Assert
        assertEquals(neighbourhood, otherNeighbourhood);
        assertEquals(neighbourhood.hashCode(), otherNeighbourhood.hashCode());
    }

    @Test
    @DisplayName("equals should return false for neighbourhoods with different names")
    public void testEqualsWithDifferentNames() {
        // Arrange
        Neighbourhood otherNeighbourhood = new Neighbourhood("Uptown");

        // Act & Assert
        assertNotEquals(neighbourhood, otherNeighbourhood);
        assertNotEquals(neighbourhood.hashCode(), otherNeighbourhood.hashCode());
    }

    @Test
    @DisplayName("equals should return false for null or different object types")
    public void testEqualsWithNullOrDifferentTypes() {
        // Act & Assert
        assertNotEquals(null, neighbourhood);
        assertNotEquals("Not a Neighbourhood", neighbourhood);
    }

    @Test
    @DisplayName("equals should consider name only, not the properties contained")
    public void testEqualsConsidersNameOnly() {
        // Arrange
        Neighbourhood neighbourhood1 = new Neighbourhood("Downtown");
        neighbourhood1.addProperty(property1);

        Neighbourhood neighbourhood2 = new Neighbourhood("Downtown");
        neighbourhood2.addProperty(property1);
        neighbourhood2.addProperty(property2); // extra property

        // Act & Assert
        assertEquals(neighbourhood1, neighbourhood2); // Should be equal despite different properties
        assertEquals(neighbourhood1.hashCode(), neighbourhood2.hashCode());
    }

    // Helper method
    private PropertyAssessment createProperty(String accountNumber, long assessedValue) {
        return new PropertyAssessment(
                accountNumber,
                new Address("", "123", "Test St"),
                "Downtown", // matching the neighbourhood name
                assessedValue,
                new Location(0, 0),
                new String[]{"Residential", "", ""},
                new double[]{100.0, 0.0, 0.0},
                "Ward 1"
        );
    }
}