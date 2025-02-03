package main.java;

public record Statistics(int count, int min, long max, long range, double mean, long median) {
    @Override
    public String toString() {
        return String.format("""
            n: %d
            Min: $%,d
            Max: $%,d
            Range: $%,d
            Mean: $%,.2f
            Median: $%,d""",
                count, min, max, range, mean, median);
    }
}