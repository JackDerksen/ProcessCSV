import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyAssessmentTest {

    @Test
    @DisplayName("Test constructor with direct parameters")
    public void testParameterizedConstructor() {
        // Arrange
        String accountNumber = "10010000";
        Address address = new Address("101", "123", "Test Street");
        String neighbourhood = "Downtown";
        long assessedValue = 500000L;
        Location location = new Location(53.5461, -113.4938);
        String[] assessmentClasses = {"Residential", "Commercial", ""};
        double[] assessmentClassPercentages = {80.0, 20.0, 0.0};
        String ward = "Ward 1";

        // Act
        PropertyAssessment assessment = new PropertyAssessment(
                accountNumber, address, neighbourhood, assessedValue,
                location, assessmentClasses, assessmentClassPercentages, ward);

        // Assert
        assertEquals(accountNumber, assessment.getAccountNumber());
        assertEquals(address, assessment.getAddress());
        assertEquals(neighbourhood, assessment.getNeighbourhood());
        assertEquals(assessedValue, assessment.getAssessedValue());
        assertEquals(location, assessment.getLocation());
        assertArrayEquals(assessmentClasses, assessment.getAssessmentClasses());
        assertEquals(ward, assessment.getWard());
        assertEquals(80.0, assessment.getAssessmentClassPercentage("Residential"));
        assertEquals(20.0, assessment.getAssessmentClassPercentage("Commercial"));
    }

    @Test
    @DisplayName("Test constructor with CSV data array")
    public void testCSVDataConstructor() {
        // Arrange
        String[] data = {
                "10010000",  // account number
                "101",       // suite
                "123",       // house number
                "Test Street", // street name
                "",         // garbage field (not used)
                "",         // garbage field (not used)
                "Downtown",  // neighbourhood
                "Ward 1",    // ward
                "500,000",   // assessed value
                "53.5461",   // latitude
                "-113.4938", // longitude
                "",         // garbage field (not used)
                "80.0",      // percentage 1
                "20.0",      // percentage 2
                "0.0",       // percentage 3
                "Residential", // class 1
                "Commercial",  // class 2
                ""            // class 3
        };

        // Act
        PropertyAssessment assessment = new PropertyAssessment(data);

        // Assert
        assertEquals("10010000", assessment.getAccountNumber());
        assertEquals("101", assessment.getAddress().getSuite());
        assertEquals("123", assessment.getAddress().getHouseNumber());
        assertEquals("Test Street", assessment.getAddress().getStreetName());
        assertEquals("Downtown", assessment.getNeighbourhood());
        assertEquals(500000L, assessment.getAssessedValue());
        assertEquals(53.5461, assessment.getLocation().latitude());
        assertEquals(-113.4938, assessment.getLocation().longitude());
        assertEquals("Ward 1", assessment.getWard());
        assertEquals(80.0, assessment.getAssessmentClassPercentage("Residential"));
        assertEquals(20.0, assessment.getAssessmentClassPercentage("Commercial"));
        assertEquals(0.0, assessment.getAssessmentClassPercentage("NonExistent"));
    }

    @Test
    @DisplayName("Test parsing assessed value with commas")
    public void testParseAssessedValue() {
        // Arrange
        String[] data = createTestData();
        data[8] = "1,234,567"; // assessed value with commas

        // Act
        PropertyAssessment assessment = new PropertyAssessment(data);

        // Assert
        assertEquals(1234567L, assessment.getAssessedValue());
    }

    @Test
    @DisplayName("Test parsing invalid assessed value")
    public void testParseInvalidAssessedValue() {
        // Arrange
        String[] data = createTestData();
        data[8] = "not a number";

        // Act
        PropertyAssessment assessment = new PropertyAssessment(data);

        // Assert
        assertEquals(0L, assessment.getAssessedValue());
    }

    @Test
    @DisplayName("Test parsing percentage values")
    public void testParsePercentages() {
        // Arrange
        String[] data = createTestData();
        data[12] = "75.5";  // Percentage 1
        data[13] = "";      // Empty percentage
        data[14] = "invalid"; // Invalid percentage

        // Act
        PropertyAssessment assessment = new PropertyAssessment(data);

        // Assert
        assertEquals(75.5, assessment.getAssessmentClassPercentage("Residential"));
        assertEquals(0.0, assessment.getAssessmentClassPercentage("Commercial")); // Empty string should be 0.0
        assertEquals(0.0, assessment.getAssessmentClassPercentage("")); // Invalid should be 0.0
    }

    @Test
    @DisplayName("Test getAssessmentClassPercentage with case insensitivity")
    public void testGetAssessmentClassPercentageCaseInsensitive() {
        // Arrange
        PropertyAssessment assessment = createSamplePropertyAssessment();

        // Act & Assert
        assertEquals(80.0, assessment.getAssessmentClassPercentage("RESIDENTIAL"));
        assertEquals(80.0, assessment.getAssessmentClassPercentage("residential"));
        assertEquals(80.0, assessment.getAssessmentClassPercentage("Residential"));
    }

    @Test
    @DisplayName("Test getAssessmentClassPercentage with null")
    public void testGetAssessmentClassPercentageWithNull() {
        // Arrange
        PropertyAssessment assessment = createSamplePropertyAssessment();

        // Act & Assert
        assertEquals(0.0, assessment.getAssessmentClassPercentage(null));
    }

    @Test
    @DisplayName("Test compareTo for sorting by assessed value")
    public void testCompareTo() {
        // Arrange
        PropertyAssessment lower = new PropertyAssessment(
                "1001", new Address("", "123", "Main St"), "Downtown",
                300000L, new Location(0, 0), new String[]{"Residential"},
                new double[]{100.0}, "Ward 1");

        PropertyAssessment higher = new PropertyAssessment(
                "1002", new Address("", "456", "Second St"), "Uptown",
                500000L, new Location(0, 0), new String[]{"Residential"},
                new double[]{100.0}, "Ward 2");

        PropertyAssessment equal = new PropertyAssessment(
                "1003", new Address("", "789", "Third St"), "Midtown",
                300000L, new Location(0, 0), new String[]{"Commercial"},
                new double[]{100.0}, "Ward 3");

        // Act & Assert
        assertTrue(lower.compareTo(higher) < 0);
        assertTrue(higher.compareTo(lower) > 0);
        assertEquals(0, lower.compareTo(equal));
    }

    @Test
    @DisplayName("Test equals and hashCode")
    public void testEqualsAndHashCode() {
        // Arrange
        PropertyAssessment assessment1 = new PropertyAssessment(
                "1001", new Address("", "123", "Main St"), "Downtown",
                300000L, new Location(0, 0), new String[]{"Residential"},
                new double[]{100.0}, "Ward 1");

        PropertyAssessment assessment2 = new PropertyAssessment(
                "1001", new Address("", "Different", "Address"), "Different",
                999999L, new Location(1, 1), new String[]{"Different"},
                new double[]{50.0}, "Different");

        PropertyAssessment assessment3 = new PropertyAssessment(
                "1002", new Address("", "123", "Main St"), "Downtown",
                300000L, new Location(0, 0), new String[]{"Residential"},
                new double[]{100.0}, "Ward 1");

        // Act & Assert
        assertEquals(assessment1, assessment2); // Same account number
        assertEquals(assessment1.hashCode(), assessment2.hashCode());

        assertNotEquals(assessment1, assessment3); // Different account number
        assertNotEquals(assessment1.hashCode(), assessment3.hashCode());

        assertNotEquals(assessment1, null);
        assertNotEquals(assessment1, "Not a PropertyAssessment");
    }

    @Test
    @DisplayName("Test toString format")
    public void testToString() {
        // Arrange
        PropertyAssessment assessment = new PropertyAssessment(
                "1001", new Address("101", "123", "Main St"), "Downtown",
                300000L, new Location(0, 0), new String[]{"Residential"},
                new double[]{100.0}, "Ward 1");

        // Act
        String result = assessment.toString();

        // Assert
        assertTrue(result.contains("1001"));
        assertTrue(result.contains("Suite 101, 123 Main St"));
        assertTrue(result.contains("300,000"));
    }

    // Helper methods
    private String[] createTestData() {
        return new String[] {
                "10010000",  // account number
                "101",       // suite
                "123",       // house number
                "Test Street", // street name
                "",         // garbage field (not used)
                "",         // garbage field (not used)
                "Downtown",  // neighbourhood
                "Ward 1",    // ward
                "500000",    // assessed value
                "53.5461",   // latitude
                "-113.4938", // longitude
                "",         // garbage field (not used)
                "80.0",      // percentage 1
                "20.0",      // percentage 2
                "0.0",       // percentage 3
                "Residential", // class 1
                "Commercial",  // class 2
                ""            // class 3
        };
    }

    private PropertyAssessment createSamplePropertyAssessment() {
        return new PropertyAssessment(
                "1001", new Address("101", "123", "Main St"), "Downtown",
                300000L, new Location(53.5461, -113.4938),
                new String[]{"Residential", "Commercial", ""},
                new double[]{80.0, 20.0, 0.0}, "Ward 1");
    }
}