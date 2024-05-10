package ca.ubc.cs304.model;
public class PublisherModel {
	private final String publisherName;
	private final String website;
	private final int pid;
	
	public PublisherModel(String publisherName, String website, int pid) {
		this.publisherName = publisherName;
		this.website = website;
		this.pid = pid;
	}

	public String getpublisherName() {
		return publisherName;
	}

	public String getWebsite() {
		return website;
	}

	public int getId() {
		return pid;
	}

}
