import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
}