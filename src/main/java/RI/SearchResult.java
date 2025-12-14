package RI;

public class SearchResult {
    private final String name;
    private final String location;
    private final double score;

    public SearchResult(String name, String location, double score) {
        this.name = name;
        this.location = location;
        this.score = score;
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public double getScore() { return score; }

    @Override
    public String toString() {
        return name + " | Location: " + location + " | Score: " + score;
    }
}