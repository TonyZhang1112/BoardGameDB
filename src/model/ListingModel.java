package ca.ubc.cs304.model;

import java.math.BigDecimal;
import java.sql.Date;
public class ListingModel {
	private final int lid;
	private final Date dateListed;
	private final BigDecimal price;
	private final String description;

	private final int boardGameID;
	private final String username;

	
	
	public ListingModel(int lid, Date dateListed, BigDecimal price, String description, int boardGameID, String username) {
		this.lid = lid;
		this.dateListed = dateListed;
		this.price = price;
		this.description = description;
		this.boardGameID = boardGameID;
		this.username = username;
	}



    public int getLid() {
		return lid;
	}
	

	public Date getDateListed(){
		return dateListed;
	}

	public BigDecimal getPrice(){
		return price;
	}

	public String getDescription(){ return description;}
	public int getBoardGameID(){return boardGameID;}

	public String getUsername(){return username;}
}
