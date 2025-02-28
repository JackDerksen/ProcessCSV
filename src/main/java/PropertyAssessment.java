import java.util.Objects;
import java.util.Arrays;

public class PropertyAssessment implements Comparable<PropertyAssessment> {
    private final String accountNumber;
    private final Address address;
    private final String neighbourhood;
    private final long assessedValue;
    private final Location location;
    private final String[] assessmentClasses;
    private final double[] assessmentClassPercentages;

    public PropertyAssessment(String accountNumber, Address address,
                              String neighbourhood, long assessedValue,
                              Location location, String[] assessmentClasses,
                              double[] assessmentClassPercentages) {
        this.accountNumber = accountNumber;
        this.address = address;
        this.neighbourhood = neighbourhood;
        this.assessedValue = assessedValue;
        this.location = location;
        this.assessmentClasses = assessmentClasses;
        this.assessmentClassPercentages = assessmentClassPercentages;
    }

    public PropertyAssessment(String[] data) {
        this.accountNumber = data[0];
        this.address = new Address(
                data[1], // suite
                data[2],              // houseNumber
                data[3]  // streetName
        );
        this.neighbourhood = data[6];
        this.assessedValue = parseAssessedValue(data[8]);
        this.location = new Location(
                Double.parseDouble(data[9]),  // latitude
                Double.parseDouble(data[10])  // longitude
        );

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
    public String getAccountNumber() { return accountNumber; }
    public Address getAddress() { return address; }
    public String getNeighbourhood() { return neighbourhood; }
    public long getAssessedValue() { return assessedValue; }
    public Location getLocation() { return location; }
    public String[] getAssessmentClasses() { return assessmentClasses; }

    public double getAssessmentClassPercentage(String className) {
        for (int i = 0; i < assessmentClasses.length; i++) {
            if (className.equals(assessmentClasses[i])) {
                return assessmentClassPercentages[i];
            }
        }
        return 0.0;
    }

    @Override
    public int compareTo(PropertyAssessment o) {
        return Long.compare(this.assessedValue, o.assessedValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyAssessment that = (PropertyAssessment) o;
        return Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public String toString() {
        return String.format("Property Assessment %s: %s - Assessed Value: $%,d",
                accountNumber, address, assessedValue);
    }
}