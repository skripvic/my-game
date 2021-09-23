package view.records;

public class Record {
    private final String name;
    private final int score;

    public Record(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String toString() {
        return name + "," + score;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }
}
