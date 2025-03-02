import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PrintReportTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        // Redirect System.out to capture printed output
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void restoreStreams() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("printPropertyAssessment should format property details correctly")
    public void testPrintPropertyAssessment() {
        // Arrange
        PropertyAssessment assessment = createSamplePropertyAssessment();

        // Act
        PrintReport.printPropertyAssessment(assessment);
        String output = outputStream.toString();

        // Assert
        assertTrue(output.contains("Assessment of property #10010000"));
        assertTrue(output.contains("Address: Suite 101, 123 Main St"));
        assertTrue(output.contains("Assessed value: $500,000"));
        assertTrue(output.contains("Assessment class: Residential (80.0%), Commercial (20.0%)"));
        assertTrue(output.contains("Neighbourhood: Downtown"));
        assertTrue(output.contains("Location: (53.546100, -113.493800)"));
    }

    @Test
    @DisplayName("printPropertyAssessment should handle null assessment")
    public void testPrintPropertyAssessmentWithNull() {
        // Act
        PrintReport.printPropertyAssessment(null);
        String output = outputStream.toString();

        // Assert
        assertEquals("Property not found." + System.lineSeparator(), output);
    }

    @Test
    @DisplayName("printHoodStats should format neighbourhood statistics correctly")
    public void testPrintHoodStats() {
        // Arrange
        String hoodName = "Downtown";
        Map<String, Object> stats = new HashMap<>();
        stats.put("count", 10);
        stats.put("mean", 500000.0);
        stats.put("median", 450000L);

        // Act
        PrintReport.printHoodStats(hoodName, stats);
        String output = outputStream.toString();

        // Assert
        assertTrue(output.contains("There are 10 properties in Downtown"));
        assertTrue(output.contains("The mean value is $500,000.00"));
        assertTrue(output.contains("The median value is $450,000"));
    }

    @Test
    @DisplayName("printHoodStats should handle empty neighbourhood")
    public void testPrintHoodStatsWithEmptyNeighbourhood() {
        // Arrange
        String hoodName = "Nonexistent";
        Map<String, Object> stats = new HashMap<>();
        stats.put("count", 0);

        // Act
        PrintReport.printHoodStats(hoodName, stats);
        String output = outputStream.toString();

        // Assert
        assertTrue(output.contains("Sorry, can't find data for Nonexistent"));
    }

    @Test
    @DisplayName("printClassStats should format assessment class statistics correctly")
    public void testPrintClassStats() {
        // Arrange
        String className = "Residential";
        Map<String, Object> stats = new HashMap<>();
        stats.put("count", 150);
        stats.put("mean", 600000.0);
        stats.put("median", 550000L);

        // Act
        PrintReport.printClassStats(className, stats, false);
        String output = outputStream.toString();

        // Assert
        assertTrue(output.contains("There are 150 Residential properties in Edmonton"));
        assertTrue(output.contains("The mean value is $600,000.00"));
        assertTrue(output.contains("The median value is $550,000"));
    }

    @Test
    @DisplayName("printClassStats with detailed stats should include min, max, and range")
    public void testPrintClassStatsWithDetailedStats() {
        // Arrange
        String className = "Residential";
        Map<String, Object> stats = new HashMap<>();
        stats.put("count", 150);
        stats.put("min", 300000L);
        stats.put("max", 900000L);
        stats.put("range", 600000L);

        // Act
        PrintReport.printClassStats(className, stats, true);
        String output = outputStream.toString();

        // Assert
        assertTrue(output.contains("There are 150 Residential properties in Edmonton"));
        assertTrue(output.contains("The min value is $300,000"));
        assertTrue(output.contains("The max value is $900,000"));
        assertTrue(output.contains("The range is $600,000"));
    }

    @Test
    @DisplayName("printClassStats should handle empty assessment class")
    public void testPrintClassStatsWithEmptyClass() {
        // Arrange
        String className = "Farm";
        Map<String, Object> stats = new HashMap<>();
        stats.put("count", 0);

        // Act
        PrintReport.printClassStats(className, stats, false);
        String output = outputStream.toString();

        // Assert
        assertTrue(output.contains("Sorry, no properties found for assessment class: Farm"));
    }

    @Test
    @DisplayName("printAllStats should format all statistics correctly")
    public void testPrintAllStats() {
        // Arrange
        String title = "City of Edmonton Property Statistics";
        Map<String, Object> stats = new HashMap<>();
        stats.put("count", 500);
        stats.put("min", 200000L);
        stats.put("max", 1000000L);
        stats.put("range", 800000L);
        stats.put("mean", 550000.0);
        stats.put("median", 520000L);

        // Act
        PrintReport.printAllStats(title, stats);
        String output = outputStream.toString();

        // Assert
        assertTrue(output.contains(title));
        assertTrue(output.contains("n: 500"));
        assertTrue(output.contains("Min: $200,000"));
        assertTrue(output.contains("Max: $1,000,000"));
        assertTrue(output.contains("Range: $800,000"));
        assertTrue(output.contains("Mean: $550,000.00"));
        assertTrue(output.contains("Median: $520,000"));
    }

    // Helper method
    private PropertyAssessment createSamplePropertyAssessment() {
        Address address = new Address("101", "123", "Main St");
        Location location = new Location(53.5461, -113.4938);
        String[] assessmentClasses = {"Residential", "Commercial", ""};
        double[] assessmentClassPercentages = {80.0, 20.0, 0.0};

        return new PropertyAssessment(
                "10010000",
                address,
                "Downtown",
                500000,
                location,
                assessmentClasses,
                assessmentClassPercentages,
                "Ward 1"
        );
    }
}