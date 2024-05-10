package ca.ubc.cs304.model;

public class BoardGameModel {
    private final String title;
    private final String genre;
    private final String description;
    private final int pid;
    private final int bid;

    public BoardGameModel(int bid, String title, String genre, String description, int pid) {
        this.title = title;
        this.genre = genre;
        this.bid = bid;
        this.description = description;
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getId() {
        return bid;
    }

    public String getDescription() {
        return description;
    }

    public int getPid() {
        return pid;
    }
}
