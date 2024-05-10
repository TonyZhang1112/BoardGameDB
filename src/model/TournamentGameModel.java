package ca.ubc.cs304.model;
public class TournamentGameModel {
	private final String rules;
	private final int bid;

	private final int minParticipants;

	public TournamentGameModel(int bid, String rules, int minParticipants) {
		this.rules = rules;
		this.bid = bid;
		this.minParticipants = minParticipants;

	}

	public String getRules(){
		return rules;
	}

	public int getId() {
		return bid;
	}

	public int getMinParticipants() {
		return minParticipants;
	}

}
