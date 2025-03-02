import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ParseCSV Tests")
class ParseCSVTest {
    private Path standardCsvFile;
    private Path emptyCsvFile;
    private Path headerOnlyCsvFile;
    private Path complexParenthesesFile;
    private Path malformedCsvFile;

    @BeforeEach
    void setUp() throws IOException {
        // Create a standard CSV file with various data patterns
        standardCsvFile = Files.createTempFile("standard", ".csv");
        List<String> standardLines = Arrays.asList(
                "Account,Suite,House,Street,Garage,ID,Hood,Ward,Value,Lat,Long,Point,P1,P2,P3,C1,C2,C3",
                "1,,123,MAIN ST,N,1,Downtown,1,100000,53.5,-113.5,,100,,,Residential,,",
                "2,101,124,MAIN ST,N,1,Downtown,1,200000,53.5,-113.5,,80,20,,Residential,Commercial,",
                "3,,125,PARK AVE,N,1,Uptown,2,300000,53.6,-113.6,,90,10,,Residential,Commercial,"
        );
        Files.write(standardCsvFile, standardLines);

        // Create a CSV file with complex parentheses patterns
        complexParenthesesFile = Files.createTempFile("complex", ".csv");
        List<String> complexLines = Arrays.asList(
                "Account,Suite,House,Street,Garage,ID,Hood,Ward,Value,Lat,Long,Point,P1,P2,P3,C1,C2,C3",
                "1,,123,MAIN ST (West, North Side),N,1,Downtown,1,100000,53.5,-113.5,,100,,,Residential,,",
                "2,101,124,COMPLEX DR (Building A, Suite 100),N,1,Downtown,1,200000,53.5,-113.5,,80,20,,Residential,Commercial,",
                "3,,125,NESTED (Level 1 (Level 2), End),N,1,Downtown,1,300000,53.5,-113.5,,90,10,,Residential,Commercial,"
        );
        Files.write(complexParenthesesFile, complexLines);

        // Create an empty CSV file (just header)
        headerOnlyCsvFile = Files.createTempFile("header", ".csv");
        List<String> headerLine = Arrays.asList(
                "Account,Suite,House,Street,Garage,ID,Hood,Ward,Value,Lat,Long,Point,P1,P2,P3,C1,C2,C3"
        );
        Files.write(headerOnlyCsvFile, headerLine);

        // Create a completely empty CSV file
        emptyCsvFile = Files.createTempFile("empty", ".csv");
        Files.write(emptyCsvFile, Arrays.asList());

        // Create a malformed CSV file with inconsistent columns
        malformedCsvFile = Files.createTempFile("malformed", ".csv");
        List<String> malformedLines = Arrays.asList(
                "Account,Suite,House,Street,Garage,ID,Hood,Ward,Value,Lat,Long,Point,P1,P2,P3,C1,C2,C3",
                "1,,123,MAIN ST,N,1,Downtown", // Missing columns
                "2,101,124,MAIN ST,N,1,Downtown,1,200000,53.5,-113.5,,80,20,,Residential,Commercial,",
                "3,,125,PARK AVE,N,1,Uptown,2,300000,53.6,-113.6,,90,10,,Residential,Commercial,Extra,Column" // Extra column
        );
        Files.write(malformedCsvFile, malformedLines);
    }

    @Test
    @DisplayName("Should correctly parse standard CSV data")
    void testReadDataStandard() throws IOException {
        // Act
        String[][] data = ParseCSV.readData(standardCsvFile);

        // Assert
        assertEquals(3, data.length, "Should have 3 rows of data");

        // First row checks
        assertEquals("1", data[0][0], "Account number should be parsed correctly");
        assertEquals("", data[0][1], "Empty suite should be parsed as empty string");
        assertEquals("123", data[0][2], "House number should be parsed correctly");
        assertEquals("MAIN ST", data[0][3], "Street name should be parsed correctly");
        assertEquals("100000", data[0][8], "Value should be parsed correctly");
        assertEquals("Residential", data[0][15], "Assessment class should be parsed correctly");

        // Verify second row
        assertEquals("2", data[1][0], "Account number for row 2");
        assertEquals("101", data[1][1], "Suite for row 2");
        assertEquals("124", data[1][2], "House number for row 2");
        assertEquals("80", data[1][12], "Percentage 1 for row 2");
        assertEquals("20", data[1][13], "Percentage 2 for row 2");
    }

    @Test
    @DisplayName("Should correctly handle commas within parentheses")
    void testReadDataWithParentheses() throws IOException {
        // Act
        String[][] data = ParseCSV.readData(complexParenthesesFile);

        // Assert
        assertEquals(3, data.length, "Should have 3 rows of data");

        // Test handling of commas within parentheses
        assertEquals("MAIN ST (West, North Side)", data[0][3],
                "Should keep commas inside parentheses");
        assertEquals("COMPLEX DR (Building A, Suite 100)", data[1][3],
                "Should handle complex address with commas inside parentheses");
        assertEquals("NESTED (Level 1 (Level 2), End)", data[2][3],
                "Should handle nested parentheses with commas");

        // Verify other fields aren't affected
        assertEquals("1", data[0][0], "Account number should still be correct");
        assertEquals("200000", data[1][8], "Value should still be correct");
        assertEquals("90", data[2][12], "Percentage should still be correct");
    }

    @Test
    @DisplayName("Should return empty array for header-only CSV")
    void testReadDataHeaderOnly() throws IOException {
        // Act
        String[][] data = ParseCSV.readData(headerOnlyCsvFile);

        // Assert
        assertEquals(0, data.length, "Should have no data rows");
    }

    @Test
    @DisplayName("Should handle completely empty CSV files")
    void testReadDataEmptyFile() throws IOException {
        // Act
        String[][] data = ParseCSV.readData(emptyCsvFile);

        // Assert
        assertEquals(0, data.length, "Should have no data rows");
    }

    @Test
    @DisplayName("Should handle malformed CSV with inconsistent columns")
    void testReadDataMalformedCsv() throws IOException {
        // Act
        String[][] data = ParseCSV.readData(malformedCsvFile);

        // Assert
        assertEquals(3, data.length, "Should have 3 rows despite malformation");

        // Check first row which has missing columns
        assertEquals("1", data[0][0], "Account number should be present");
        assertEquals("Downtown", data[0][6], "Neighborhood should be present");

        // Verify length of each row matches the available data
        assertTrue(data[0].length < data[1].length,
                "First row should have fewer fields due to missing columns");

        // Extra column row
        assertEquals("3", data[2][0], "Account number for row with extra column");
        assertEquals("Extra", data[2][18], "Should include extra column at the end");
        assertEquals("Column", data[2][19], "Should include last extra column");
    }

    @Test
    @DisplayName("Should throw IOException for non-existent file")
    void testReadDataNonExistentFile() {
        // Arrange
        Path nonExistentFile = Paths.get("non_existent_file.csv");

        // Act & Assert
        assertThrows(IOException.class, () -> ParseCSV.readData(nonExistentFile),
                "Should throw IOException for non-existent file");
    }

    @ParameterizedTest
    @DisplayName("Should throw IOException for invalid paths")
    @ValueSource(strings = {"", "invalid/path/with:special?chars"})
    void testReadDataInvalidPaths(String invalidPath) {
        // Arrange
        Path path = Paths.get(invalidPath);

        // Act & Assert
        assertThrows(IOException.class, () -> ParseCSV.readData(path),
                "Should throw IOException for invalid path: " + invalidPath);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up all temporary files
        Files.deleteIfExists(standardCsvFile);
        Files.deleteIfExists(complexParenthesesFile);
        Files.deleteIfExists(headerOnlyCsvFile);
        Files.deleteIfExists(emptyCsvFile);
        Files.deleteIfExists(malformedCsvFile);
    }
}