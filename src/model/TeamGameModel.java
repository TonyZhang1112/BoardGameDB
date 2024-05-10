package ca.ubc.cs304.model;
public class TeamGameModel {
	private final int minTeamSize;
	private final int bid;
	
	
	public TeamGameModel(int minTeamSize, int bid) {
		this.minTeamSize = minTeamSize;
		this.bid = bid;
	}

	public int getMinTeamSize() {
		return minTeamSize;
	}
	

	public int getId() {
		return bid;
	}
}
