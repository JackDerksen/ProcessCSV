public class Property {
    private final String accountNumber;
    private final String suite;
    private final String houseNumber;
    private final String streetName;
    private final String neighbourhood;
    private final double latitude;
    private final double longitude;

    public Property(String accountNumber, String suite, String houseNumber,
                    String streetName, String neighbourhood,
                    double latitude, double longitude) {
        this.accountNumber = accountNumber;
        this.suite = suite;
        this.houseNumber = houseNumber;
        this.streetName = streetName;
        this.neighbourhood = neighbourhood;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Simplified constructor for creating from CSV data
    public Property(String[] data) {
        this.accountNumber = data[0];
        this.suite = data[1];
        this.houseNumber = data[2];
        this.streetName =data[3];
        this.neighbourhood =data[6];
        this.latitude = Double.parseDouble(data[9]);
        this.longitude = Double.parseDouble(data[10]);
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getSuite() { return suite; }
    public String getHouseNumber() { return houseNumber; }
    public String getStreetName() { return streetName; }
    public String getNeighbourhood() { return neighbourhood; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    // Composite getters
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

    @Override
    public String toString() {
        return String.format("Property %s: %s in %s",
                accountNumber, getAddress(), neighbourhood);
    }
}