import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class LocationTest {

    @Test
    @DisplayName("Constructor should set values correctly")
    public void testConstructorSetsValues() {
        // Arrange
        double latitude = 53.5461;
        double longitude = -113.4938;

        // Act
        Location location = new Location(latitude, longitude);

        // Assert
        assertEquals(latitude, location.latitude());
        assertEquals(longitude, location.longitude());
    }

    @Test
    @DisplayName("toString should format location correctly")
    public void testToString() {
        // Arrange
        Location location = new Location(53.5461, -113.4938);

        // Act & Assert
        assertEquals("(53.546100, -113.493800)", location.toString());
    }

    @Test
    @DisplayName("equals should return true for identical locations")
    public void testEqualsWithIdenticalLocations() {
        // Arrange
        Location location1 = new Location(53.5461, -113.4938);
        Location location2 = new Location(53.5461, -113.4938);

        // Act & Assert
        assertEquals(location1, location2);
        assertEquals(location1.hashCode(), location2.hashCode());
    }

    @Test
    @DisplayName("equals should return false for different locations")
    public void testEqualsWithDifferentLocations() {
        // Arrange
        Location location1 = new Location(53.5461, -113.4938);
        Location location2 = new Location(53.5462, -113.4938);
        Location location3 = new Location(53.5461, -113.4939);

        // Act & Assert
        assertNotEquals(location1, location2);
        assertNotEquals(location1, location3);
    }

    @Test
    @DisplayName("equals should return false for null or different object types")
    public void testEqualsWithNullOrDifferentTypes() {
        // Arrange
        Location location = new Location(53.5461, -113.4938);

        // Act & Assert
        assertNotEquals(null, location);
        assertNotEquals("Not a Location", location);
    }

    @Test
    @DisplayName("Test edge case coordinates")
    public void testEdgeCaseCoordinates() {
        // Arrange & Act
        Location northPole = new Location(90.0, 0.0);
        Location southPole = new Location(-90.0, 0.0);
        Location dateLine = new Location(0.0, 180.0);
        Location negDateLine = new Location(0.0, -180.0);

        // Assert
        assertEquals(90.0, northPole.latitude());
        assertEquals(-90.0, southPole.latitude());
        assertEquals(180.0, dateLine.longitude());
        assertEquals(-180.0, negDateLine.longitude());
    }

    @Test
    @DisplayName("hashCode should be consistent")
    public void testHashCodeConsistency() {
        // Arrange
        Location location = new Location(53.5461, -113.4938);

        // Act & Assert
        int hashCode1 = location.hashCode();
        int hashCode2 = location.hashCode();
        assertEquals(hashCode1, hashCode2);
    }
}