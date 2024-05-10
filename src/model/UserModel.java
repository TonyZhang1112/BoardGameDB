package ca.ubc.cs304.model;

public class UserModel {
	private final String username;
	private final String password;
	private final String email;
	private final int wid;
	private final String address;
	private final String postalCode;
	private final String country;
	
	
	public UserModel(String username, String password, String email, int wid, String address, String postalCode, String country) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.wid = wid;
		this.address = address;
		this.postalCode = postalCode;
		this.country = country;
	}

	public String getUsername(){
		return username;
	}
	public String getEmail(){
		return email;
	}
	public String getPassword(){
		return password;
	}
	public int getWid(){
		return wid;
	}
	public String getAddress(){
		return address;
	}
	public String getPostalCode(){
		return postalCode;
	}
	public String getCountry(){return country;}
}
