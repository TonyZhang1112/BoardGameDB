package ca.ubc.cs304.model;

import java.sql.Date;

public class GameEventSeriesModel {
	private final String seriesName;
	private final String location;
	private final Date startDate;
	private final Date endDate;
	private final String creatorName;
	
	
	public GameEventSeriesModel(String seriesName, String location, Date startDate, Date endDate, String creatorName) {
		this.seriesName = seriesName;
		this.location = location;
		this.startDate = startDate;
		this.endDate = endDate;
		this.creatorName = creatorName;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public String getLocation() {
		return location;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate(){
		return endDate;
	}

	public String getCreatorName(){
		return creatorName;
	}
}