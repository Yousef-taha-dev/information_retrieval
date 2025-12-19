package RI;

public class SearchResult {
    private final int id;
    private final String name;
    private final String location;
    private final double score;
    private final String description; // 🔹 إضافة هذا الحقل

    public SearchResult(int id, String name, String location, double score, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.score = score;
        this.description = description; // 🔹 تعيين القيمة
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public double getScore() { return score; }
    public String getDescription() { return description; } // 🔹 الميثود التي كانت مفقودة
}
