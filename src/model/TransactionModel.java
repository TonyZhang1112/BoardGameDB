package ca.ubc.cs304.model;

import java.math.BigDecimal;
import java.sql.Date;

public class TransactionModel {
	private final int tid;
	private final BigDecimal amount;
	private final String buyer;
	private final String seller;
	private final Date date;
	
	
	public TransactionModel(int tid, BigDecimal amount, String buyer, String seller, Date date) {
		this.tid = tid;
		this.amount = amount;
		this.buyer = buyer;
		this.seller = seller;
		this.date = date;
	}

	public int getTid() {
		return tid;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getBuyer(){
		return buyer;
	}
	
	public String getSeller(){
		return seller;
	}

	public Date getDate(){
		return date;
	}
}
