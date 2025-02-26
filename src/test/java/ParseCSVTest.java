import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParseCSVTest {
    private Path tempFile;

    /// Create a short test CSV list
    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary CSV file for testing
        tempFile = Files.createTempFile("test", ".csv");
        List<String> lines = Arrays.asList(
                "Account,Suite,House,Street,Garage,ID,Hood,Ward,Value,Lat,Long,Point,P1,P2,P3,C1,C2,C3",
                "1,,123,MAIN ST,N,1,Downtown,1,100000,53.5,-113.5,,100,,,Residential,,",
                "2,101,124,MAIN ST (West, North Side),N,1,Downtown,1,200000,53.5,-113.5,,80,20,,Residential,Commercial,",
                "3,,125,Complex Drive (Building A, Suite 100),N,1,Downtown,1,300000,53.5,-113.5,,90,10,,Residential,Commercial,"
        );
        Files.write(tempFile, lines);
    }

    /// Verifies correct CSV parsing
    @Test
    void testReadData() throws IOException {
        String[][] data = ParseCSV.readData(tempFile);

        // Test basic parsing
        assertEquals(3, data.length);
        assertEquals("1", data[0][0]);
        assertEquals("MAIN ST", data[0][3]);

        // Test handling of commas within parentheses
        assertEquals("MAIN ST (West, North Side)", data[1][3]);
        assertEquals("Complex Drive (Building A, Suite 100)", data[2][3]);

        // Verify other fields are still parsed correctly
        assertEquals("101", data[1][1]);    // Suite number
        assertEquals("124", data[1][2]);    // House number
        assertEquals("200000", data[1][8]); // Value
    }

    /// Verifies that the proper IOException is thrown for an invalid file name
    @Test
    void testReadDataWithInvalidFile() {
        Path nonexistentFile = Paths.get("nonexistent.csv");
        assertThrows(IOException.class, () -> ParseCSV.readData(nonexistentFile));
    }

    /// Delete the temporary test files after running each test, if they exist
    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }
}