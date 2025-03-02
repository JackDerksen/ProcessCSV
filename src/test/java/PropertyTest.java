import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyTest {

    @Test
    @DisplayName("Constructor should set values correctly")
    public void testConstructor() {
        // Arrange
        String accountNumber = "10010000";
        String suite = "101";
        String houseNumber = "123";
        String streetName = "Main St";
        String neighbourhood = "Downtown";
        double latitude = 53.5461;
        double longitude = -113.4938;

        // Act
        Property property = new Property(accountNumber, suite, houseNumber,
                streetName, neighbourhood, latitude, longitude);

        // Assert
        assertEquals(accountNumber, property.getAccountNumber());
        assertEquals(suite, property.getSuite());
        assertEquals(houseNumber, property.getHouseNumber());
        assertEquals(streetName, property.getStreetName());
        assertEquals(neighbourhood, property.getNeighbourhood());
        assertEquals(latitude, property.getLatitude(), 0.00001);
        assertEquals(longitude, property.getLongitude(), 0.00001);
    }

    @Test
    @DisplayName("Constructor with CSV data should set values correctly")
    public void testCSVDataConstructor() {
        // Arrange
        String[] data = {
                "10010000",  // account number
                "101",       // suite
                "123",       // house number
                "Main St",   // street name
                "",         // garbage field (not used)
                "",         // garbage field (not used)
                "Downtown",  // neighbourhood
                "",         // garbage field (not used)
                "",         // garbage field (not used)
                "53.5461",   // latitude
                "-113.4938"  // longitude
        };

        // Act
        Property property = new Property(data);

        // Assert
        assertEquals("10010000", property.getAccountNumber());
        assertEquals("101", property.getSuite());
        assertEquals("123", property.getHouseNumber());
        assertEquals("Main St", property.getStreetName());
        assertEquals("Downtown", property.getNeighbourhood());
        assertEquals(53.5461, property.getLatitude(), 0.00001);
        assertEquals(-113.4938, property.getLongitude(), 0.00001);
    }

    @Test
    @DisplayName("getAddress should format address correctly with suite")
    public void testGetAddressWithSuite() {
        // Arrange
        Property property = new Property("10010000", "101", "123", "Main St",
                "Downtown", 53.5461, -113.4938);

        // Act
        String address = property.getAddress();

        // Assert
        assertEquals("Suite 101, 123 Main St", address);
    }

    @Test
    @DisplayName("getAddress should format address correctly without suite")
    public void testGetAddressWithoutSuite() {
        // Arrange
        Property property = new Property("10010000", "", "123", "Main St",
                "Downtown", 53.5461, -113.4938);

        // Act
        String address = property.getAddress();

        // Assert
        assertEquals("123 Main St", address);
    }

    @Test
    @DisplayName("getLocation should format location coordinates correctly")
    public void testGetLocation() {
        // Arrange
        Property property = new Property("10010000", "101", "123", "Main St",
                "Downtown", 53.5461, -113.4938);

        // Act
        String location = property.getLocation();

        // Assert
        assertEquals("(53.546100, -113.493800)", location);
    }

    @Test
    @DisplayName("toString should format property information correctly")
    public void testToString() {
        // Arrange
        Property property = new Property("10010000", "101", "123", "Main St",
                "Downtown", 53.5461, -113.4938);

        // Act
        String result = property.toString();

        // Assert
        assertEquals("Property 10010000: Suite 101, 123 Main St in Downtown", result);
    }

    // Fixed the parameterized test that was causing failures
    @ParameterizedTest
    @DisplayName("getAddress should handle various suite and address combinations")
    @CsvSource(delimiter = '|', value = {
            "''|123|Main St|123 Main St",
            "''|''|Main St| Main St",
            "101|123|Main St|Suite 101, 123 Main St",
            "A|456|Park Ave|Suite A, 456 Park Ave"
    })
    public void testGetAddressWithVariousInputs(String suite, String houseNumber,
                                                String streetName, String expected) {
        // Arrange
        Property property = new Property("1001", suite, houseNumber, streetName,
                "Test", 0, 0);

        // Act & Assert
        assertEquals(expected, property.getAddress());
    }

    @Test
    @DisplayName("getLocation should format with 6 decimal places")
    public void testGetLocationFormatting() {
        // Arrange
        Property property = new Property("1001", "", "", "", "", 12.3456789, -98.7654321);

        // Act
        String location = property.getLocation();

        // Assert
        assertEquals("(12.345679, -98.765432)", location);
    }
}