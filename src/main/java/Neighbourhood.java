import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a neighborhood containing property assessments, with methods
 * to calculate statistics and manage properties within the neighborhood.
 */

public class Neighbourhood {
    private final String name;
    private final List<PropertyAssessment> properties;

    public Neighbourhood(String name) {
        this.name = name;
        this.properties = new ArrayList<>();
    }

    public void addProperty(PropertyAssessment property) {
        properties.add(property);
    }

    public String getName() { return name; }
    public int getCount() { return properties.size(); }
    public List<PropertyAssessment> getProperties() { return properties; }

    public Map<String, Object> getStatistics() {
        return CalculateStatistics.calculateNeighbourhoodStats(properties);
    }

    @Override
    public String toString() {
        return String.format("%s (%d properties)", name, properties.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Neighbourhood that = (Neighbourhood) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}