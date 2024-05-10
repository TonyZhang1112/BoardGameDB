package ca.ubc.cs304.model;

public class HostsPerUser {
    private final String username;
    private final int numHosts;

    public HostsPerUser(String username, int numHosts) {
        this.username = username;
        this.numHosts = numHosts;
    }

    public String getUsername() {
        return username;
    }

    public int getNumHosts() {
        return numHosts;
    }
}
