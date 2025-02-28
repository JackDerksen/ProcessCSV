import java.util.Objects;

public class Address {
    private final String suite;
    private final String houseNumber;
    private final String streetName;

    public Address(String suite, String houseNumber, String streetName) {
        this.suite = suite != null ? suite : "";
        this.houseNumber = houseNumber != null ? houseNumber : "";
        this.streetName = streetName != null ? streetName : "";
    }

    public String getSuite() { return suite; }
    public String getHouseNumber() { return houseNumber; }
    public String getStreetName() { return streetName; }

    @Override
    public String toString() {
        StringBuilder address = new StringBuilder();
        if (!suite.isEmpty()) {
            address.append("Suite ").append(suite).append(", ");
        }
        address.append(houseNumber).append(" ").append(streetName);
        return address.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(suite, address.suite) &&
                Objects.equals(houseNumber, address.houseNumber) &&
                Objects.equals(streetName, address.streetName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suite, houseNumber, streetName);
    }
}