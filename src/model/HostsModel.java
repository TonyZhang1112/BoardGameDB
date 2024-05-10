package ca.ubc.cs304.model;

public class HostsModel {
    private final int sessionNum;
    private final String username;
    private final String seriesName;

    public HostsModel(String username, int sessionNum, String seriesName) {
        this.username = username;
        this.sessionNum = sessionNum;
        this.seriesName = seriesName;
    }

    public int getSessionNum() {
        return sessionNum;
    }

    public String getUsername() {
        return username;
    }

    public String getSeriesName() {
        return seriesName;
    }
}
