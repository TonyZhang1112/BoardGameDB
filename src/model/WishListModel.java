package ca.ubc.cs304.model;

import java.sql.Date;

public class WishListModel {
	private final Date dateUpdated;
	private final int wid;
	
	public WishListModel(int wid, Date dateUpDated) {
		this.dateUpdated = dateUpDated;
		this.wid = wid;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public int getWid() {
		return wid;
	}
}
