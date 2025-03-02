import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HoodAssessmentTest {

    private Map<String, Object> statsMap;

    @BeforeEach
    public void setUp() {
        // Create a sample statistics map with test data
        statsMap = new HashMap<>();
        statsMap.put("count", 10);
        statsMap.put("mean", 500000.0);
        statsMap.put("median", 450000L);
        statsMap.put("min", 300000L);
        statsMap.put("max", 800000L);
        statsMap.put("range", 500000L);
    }

    @Test
    @DisplayName("Constructor with stats map should set values correctly")
    public void testConstructorWithStatsMap() {
        // Act
        HoodAssessment assessment = new HoodAssessment("Test Neighbourhood", statsMap);

        // Assert
        assertEquals("Test Neighbourhood", assessment.getName());
        assertEquals(10, assessment.getPropertyCount());
        assertEquals(500000.0, assessment.getMeanValue(), 0.01);
        assertEquals(450000L, assessment.getMedianValue());
        assertEquals(300000L, assessment.getMinValue());
        assertEquals(800000L, assessment.getMaxValue());
        assertEquals(500000L, assessment.getValueRange());
    }

    @Test
    @DisplayName("Constructor with stats map should handle missing values")
    public void testConstructorWithMissingStats() {
        // Arrange
        Map<String, Object> incompleteStats = new HashMap<>();
        incompleteStats.put("count", 5);
        // Other stats are missing

        // Act
        HoodAssessment assessment = new HoodAssessment("Test Neighbourhood", incompleteStats);

        // Assert
        assertEquals("Test Neighbourhood", assessment.getName());
        assertEquals(5, assessment.getPropertyCount());
        assertEquals(0.0, assessment.getMeanValue(), 0.01);
        assertEquals(0L, assessment.getMedianValue());
        assertEquals(0L, assessment.getMinValue());
        assertEquals(0L, assessment.getMaxValue());
        assertEquals(0L, assessment.getValueRange());
    }

    @Test
    @DisplayName("Constructor with Neighbourhood should calculate stats correctly")
    public void testConstructorWithNeighbourhood() {
        // Arrange
        Neighbourhood neighbourhood = new Neighbourhood("Downtown");

        // Add properties to the neighbourhood
        neighbourhood.addProperty(createProperty("1001", 300000));
        neighbourhood.addProperty(createProperty("1002", 500000));
        neighbourhood.addProperty(createProperty("1003", 700000));

        // Act
        HoodAssessment assessment = new HoodAssessment(neighbourhood);

        // Assert
        assertEquals("Downtown", assessment.getName());
        assertEquals(3, assessment.getPropertyCount());
        assertEquals(500000.0, assessment.getMeanValue(), 0.01);
        assertEquals(500000L, assessment.getMedianValue());
        assertEquals(300000L, assessment.getMinValue());
        assertEquals(700000L, assessment.getMaxValue());
        assertEquals(400000L, assessment.getValueRange());
    }

    @Test
    @DisplayName("Constructor with empty Neighbourhood should initialize with zeros")
    public void testConstructorWithEmptyNeighbourhood() {
        // Arrange
        Neighbourhood emptyNeighbourhood = new Neighbourhood("Empty");

        // Act
        HoodAssessment assessment = new HoodAssessment(emptyNeighbourhood);

        // Assert
        assertEquals("Empty", assessment.getName());
        assertEquals(0, assessment.getPropertyCount());
        assertEquals(0.0, assessment.getMeanValue(), 0.01);
        assertEquals(0L, assessment.getMedianValue());
        assertEquals(0L, assessment.getMinValue());
        assertEquals(0L, assessment.getMaxValue());
        assertEquals(0L, assessment.getValueRange());
    }

    @Test
    @DisplayName("toString should format assessment summary correctly")
    public void testToString() {
        // Arrange
        HoodAssessment assessment = new HoodAssessment("Test Neighbourhood", statsMap);

        // Act
        String result = assessment.toString();

        // Assert
        String expected = "Test Neighbourhood: 10 properties, Mean: $500,000.00, Median: $450,000";
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("printSummary should output correct information")
    public void testPrintSummary() {
        // Arrange
        HoodAssessment assessment = new HoodAssessment("Test Neighbourhood", statsMap);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            assessment.printSummary();

            // Assert
            String output = outputStream.toString();
            assertTrue(output.contains("Assessment of neighbourhood: Test Neighbourhood"));
            assertTrue(output.contains("Number of properties: 10"));
            assertTrue(output.contains("Mean assessed value: $500,000.00"));
            assertTrue(output.contains("Median assessed value: $450,000"));
            assertTrue(output.contains("Min value: $300,000"));
            assertTrue(output.contains("Max value: $800,000"));
            assertTrue(output.contains("Range: $500,000"));
        } finally {
            // Restore the original System.out
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("printSummary should handle empty neighbourhoods")
    public void testPrintSummaryWithEmptyNeighbourhood() {
        // Arrange
        Map<String, Object> emptyStats = new HashMap<>();
        emptyStats.put("count", 0);
        emptyStats.put("mean", 0.0);
        emptyStats.put("median", 0L);
        emptyStats.put("min", 0L);
        emptyStats.put("max", 0L);
        emptyStats.put("range", 0L);

        HoodAssessment assessment = new HoodAssessment("Empty", emptyStats);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            assessment.printSummary();

            // Assert
            String output = outputStream.toString();
            assertTrue(output.contains("Assessment of neighbourhood: Empty"));
            assertTrue(output.contains("Number of properties: 0"));
            assertTrue(output.contains("Mean assessed value: $0.00"));
            assertTrue(output.contains("Median assessed value: $0"));
            // Should not contain min, max, range for empty neighbourhoods
            assertFalse(output.contains("Min value"));
            assertFalse(output.contains("Max value"));
            assertFalse(output.contains("Range"));
        } finally {
            // Restore the original System.out
            System.setOut(originalOut);
        }
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