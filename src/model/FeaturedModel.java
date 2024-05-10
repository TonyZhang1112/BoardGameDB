package ca.ubc.cs304.model;

public class FeaturedModel {
    private final int sessionNum;
    private final int boardGameId;
    private final String seriesName;

    public FeaturedModel(int boardGameId, int sessionNum, String seriesName) {
        this.boardGameId = boardGameId;
        this.sessionNum = sessionNum;
        this.seriesName = seriesName;
    }

    public int getSessionNum() {
        return sessionNum;
    }

    public int getBoardGameId() {
        return boardGameId;
    }

    public String getSeriesName() {
        return seriesName;
    }
}
