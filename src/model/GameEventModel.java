package ca.ubc.cs304.model;

import oracle.sql.DATE;

import java.sql.Date;

public class GameEventModel {
	private final int sessionNum;
	private final String seriesName;
	private final String location;
	private final Date date;
	
	
	public GameEventModel(int sessionNum, String seriesName, String location, Date date) {
		this.sessionNum = sessionNum;
		this.seriesName = seriesName;
		this.location = location;
		this.date = date;
	}

	public int getSessionNum() {
		return sessionNum;
	}
	

	public String getSeriesName() {
		return seriesName;
	}

	public String getLocation() {
		return location;
	}

	public Date getDate() {
		return date;
	}
}
