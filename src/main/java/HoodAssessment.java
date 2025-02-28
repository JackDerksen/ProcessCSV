import java.util.Map;

public class HoodAssessment {
    private final String name;
    private final int propertyCount;
    private final double meanValue;
    private final long medianValue;
    private final long minValue;
    private final long maxValue;
    private final long valueRange;

    public HoodAssessment(String name, Map<String, Object> stats) {
        this.name = name;
        this.propertyCount = (Integer) stats.getOrDefault("count", 0);
        this.meanValue = (Double) stats.getOrDefault("mean", 0.0);
        this.medianValue = (Long) stats.getOrDefault("median", 0L);
        this.minValue = (Long) stats.getOrDefault("min", 0L);
        this.maxValue = (Long) stats.getOrDefault("max", 0L);
        this.valueRange = (Long) stats.getOrDefault("range", 0L);
    }

    // Another constructor that takes a Neighbourhood and calculates stats directly
    public HoodAssessment(Neighbourhood neighbourhood) {
        this.name = neighbourhood.getName();
        Map<String, Object> stats = CalculateStatistics.calculateAllStats(neighbourhood.getProperties());

        this.propertyCount = (Integer) stats.getOrDefault("count", 0);
        this.meanValue = (Double) stats.getOrDefault("mean", 0.0);
        this.medianValue = (Long) stats.getOrDefault("median", 0L);
        this.minValue = (Long) stats.getOrDefault("min", 0L);
        this.maxValue = (Long) stats.getOrDefault("max", 0L);
        this.valueRange = (Long) stats.getOrDefault("range", 0L);
    }

    // Getters
    public String getName() { return name; }
    public int getPropertyCount() { return propertyCount; }
    public double getMeanValue() { return meanValue; }
    public long getMedianValue() { return medianValue; }
    public long getMinValue() { return minValue; }
    public long getMaxValue() { return maxValue; }
    public long getValueRange() { return valueRange; }

    // Print to console method
    public void printSummary() {
        System.out.println("Assessment of neighbourhood: " + name);
        System.out.println("-------------------------------");
        System.out.println("Number of properties: " + propertyCount);
        System.out.printf("Mean assessed value: $%,.2f%n", meanValue);
        System.out.printf("Median assessed value: $%,d%n", medianValue);
        if (propertyCount > 0) {
            System.out.printf("Min value: $%,d%n", minValue);
            System.out.printf("Max value: $%,d%n", maxValue);
            System.out.printf("Range: $%,d%n", valueRange);
        }
    }

    @Override
    public String toString() {
        return String.format("%s: %d properties, Mean: $%,.2f, Median: $%,d",
                name, propertyCount, meanValue, medianValue);
    }
}