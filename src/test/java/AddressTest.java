import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class AddressTest {

    @Test
    @DisplayName("Constructor should set values correctly")
    public void testConstructorSetsValues() {
        // Arrange
        String suite = "101";
        String houseNumber = "123";
        String streetName = "Main St";

        // Act
        Address address = new Address(suite, houseNumber, streetName);

        // Assert
        assertEquals(suite, address.getSuite());
        assertEquals(houseNumber, address.getHouseNumber());
        assertEquals(streetName, address.getStreetName());
    }

    @Test
    @DisplayName("Constructor should handle null values")
    public void testConstructorHandlesNulls() {
        // Act
        Address address = new Address(null, null, null);

        // Assert
        assertEquals("", address.getSuite());
        assertEquals("", address.getHouseNumber());
        assertEquals("", address.getStreetName());
    }

    @ParameterizedTest
    @DisplayName("toString should format address correctly with suite")
    @CsvSource({
            "101, 123, Main St, Suite 101, 123 Main St",
            "A, 456, Park Ave, Suite A, 456 Park Ave"
    })
    public void testToStringWithSuite(String suite, String houseNumber, String streetName, String expected) {
        // Arrange
        Address address = new Address(suite, houseNumber, streetName);

        // Act & Assert
        assertEquals(expected, address.toString());
    }

    @Test
    @DisplayName("toString should format address correctly without suite")
    public void testToStringWithoutSuite() {
        // Arrange
        Address address = new Address("", "123", "Main St");

        // Act & Assert
        assertEquals("123 Main St", address.toString());
    }

    @Test
    @DisplayName("equals should return true for identical addresses")
    public void testEqualsWithIdenticalAddresses() {
        // Arrange
        Address address1 = new Address("101", "123", "Main St");
        Address address2 = new Address("101", "123", "Main St");

        // Act & Assert
        assertEquals(address1, address2);
        assertEquals(address1.hashCode(), address2.hashCode());
    }

    @Test
    @DisplayName("equals should return false for different addresses")
    public void testEqualsWithDifferentAddresses() {
        // Arrange
        Address address1 = new Address("101", "123", "Main St");
        Address address2 = new Address("102", "123", "Main St");
        Address address3 = new Address("101", "124", "Main St");
        Address address4 = new Address("101", "123", "Broadway");

        // Act & Assert
        assertNotEquals(address1, address2);
        assertNotEquals(address1, address3);
        assertNotEquals(address1, address4);
    }

    @Test
    @DisplayName("equals should return false for null or different object types")
    public void testEqualsWithNullOrDifferentTypes() {
        // Arrange
        Address address = new Address("101", "123", "Main St");

        // Act & Assert
        assertNotEquals(null, address);
        assertNotEquals("Not an Address", address);
    }

    @Test
    @DisplayName("hashCode should be consistent")
    public void testHashCodeConsistency() {
        // Arrange
        Address address = new Address("101", "123", "Main St");

        // Act & Assert
        int hashCode1 = address.hashCode();
        int hashCode2 = address.hashCode();
        assertEquals(hashCode1, hashCode2);
    }
}