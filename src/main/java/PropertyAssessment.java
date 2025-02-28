import java.util.Objects;
import java.util.Arrays;

public class PropertyAssessment extends Property implements Comparable<PropertyAssessment> {
    private final long assessedValue;
    private final String[] assessmentClasses;
    private final double[] assessmentClassPercentages;

    public PropertyAssessment(String accountNumber, String suite, String houseNumber,
                              String streetName, String neighbourhood,
                              double latitude, double longitude,
                              long assessedValue, String[] assessmentClasses,
                              double[] assessmentClassPercentages) {
        super(accountNumber, suite, houseNumber, streetName, neighbourhood, latitude, longitude);
        this.assessedValue = assessedValue;
        this.assessmentClasses = assessmentClasses;
        this.assessmentClassPercentages = assessmentClassPercentages;
    }

    public PropertyAssessment(String[] data) {
        super(data);
        this.assessedValue = parseAssessedValue(data[8]);

        this.assessmentClasses = new String[]{
                data[15],
                data[16],
                data[17]
        };

        this.assessmentClassPercentages = new double[]{
                parsePercentage(data[12]),
                parsePercentage(data[13]),
                parsePercentage(data[14]),
        };
    }

    private long parseAssessedValue(String value) {
        try {
            return Long.parseLong(value.replace(",", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parsePercentage(String value) {
        try {
            return value.isEmpty() ? 0.0 : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // Getters
    public long getAssessedValue() { return assessedValue; }
    public String[] getAssessmentClasses() { return assessmentClasses; }

    // Helper methods
    public double getAssessmentClassPercentage(String className) {
        for (int i = 0; i < assessmentClasses.length; i++) {
            if (className.equals(assessmentClasses[i])) {
                return assessmentClassPercentages[i];
            }
        }
        return 0.0;  // Return 0 if class not found
    }

    // For sorting by assessed value
    @Override
    public int compareTo(PropertyAssessment o) {
        return Long.compare(this.assessedValue, o.getAssessedValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyAssessment that = (PropertyAssessment) o;
        return Objects.equals(getAccountNumber(), that.getAccountNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountNumber());
    }

    @Override
    public String toString() {
        return String.format("Property Assessment %s: %s - Assessed Value: $%,d",
                getAccountNumber(), getAddress(), assessedValue);
    }
}