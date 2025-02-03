package main.java;

import java.util.Objects;

public class PropertyAssessment implements Comparable<PropertyAssessment> {
    private final String accountNumber;
    private final String suite;
    private final String houseNumber;
    private final String streetName;
    private final String neighbourhood;
    private final long assessedValue;
    private final double latitude;
    private final double longitude;
    private final String[] assessmentClasses;

    public PropertyAssessment(String[] data) {
        this.accountNumber = data[0];
        this.suite = toTitleCase(data[1]);
        this.houseNumber = data[2];
        this.streetName = toTitleCase(data[3]);
        boolean hasGarage = data[4].equals("Y");
        String neighbourhoodId = data[5];
        this.neighbourhood = toTitleCase(data[6]);
        String ward = data[7];
        this.assessedValue = parseAssessedValue(data[8]);
        this.latitude = Double.parseDouble(data[9]);
        this.longitude = Double.parseDouble(data[10]);

        // Handle assessment classes and their percentages
        this.assessmentClasses = new String[]{toTitleCase(data[15]), toTitleCase(data[16]), toTitleCase(data[17])};
        double[] assessmentClassPercentages = new double[]{
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

    private String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Split on spaces and handle each word
        String[] words = input.toLowerCase().split("\\s+");
        StringBuilder titleCase = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                // Skip title casing for specific words if they're not at the start
                if (!titleCase.isEmpty() &&
                        (word.equals("of") || word.equals("the") ||
                                word.equals("in") || word.equals("and"))) {
                    titleCase.append(word.toLowerCase()).append(" ");
                } else {
                    // Capitalize first letter, keep rest lowercase
                    titleCase.append(Character.toUpperCase(word.charAt(0)))
                            .append(word.substring(1))
                            .append(" ");
                }
            }
        }

        return titleCase.toString().trim();
    }

    public String getAccountNumber() { return accountNumber; }

    public String getNeighbourhood() { return neighbourhood; }

    public long getAssessedValue() { return assessedValue; }

    public String getAddress() {
        StringBuilder address = new StringBuilder();
        if (!suite.isEmpty()) {
            address.append("Suite ").append(suite).append(", ");
        }
        address.append(houseNumber).append(" ").append(streetName);
        return address.toString();
    }

    public String getLocation() {
        return String.format("(%.6f, %.6f)", latitude, longitude);
    }

    public String[] getAssessmentClasses() { return assessmentClasses; }

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
                accountNumber, getAddress(), assessedValue);
    }
}
